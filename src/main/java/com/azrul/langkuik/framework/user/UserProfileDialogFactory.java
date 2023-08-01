/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.user;

import com.azrul.langkuik.framework.standard.LangkuikMultiFileBuffer;
import com.azrul.langkuik.framework.dao.DataAccessObject;
import com.azrul.langkuik.framework.dao.query.FindAnyEntityQuery;
import com.azrul.langkuik.framework.dao.filter.AndFilters;
import com.azrul.langkuik.framework.dao.filter.FilterRelation;
import com.azrul.langkuik.framework.dao.filter.QueryFilter;
import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.minio.MinioService;
import com.azrul.langkuik.framework.entity.Status;
import com.azrul.langkuik.views.pojo.PojoView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author azrul
 */
@Component
public class UserProfileDialogFactory {

    @Autowired
    private DataAccessObject daoUserProfile;

    @Value("${application.lgDateFormat:yyyy-MM-dd}")
    private String dateFormat;

    @Value("${application.lgProfilePicDimension}")
    private int profilePicDimension;

    @Value("${application.lgProfilePicMaxUploadSize}")
    private int profilePicMaxUploadSize;

    @Value("${application.lgProfilePicBaseMinioDir}")
    private String profilePicBaseMinioDir;

    @Autowired
    private MinioService minioService;

    public void createDialog() {
        Dialog dialog2 = new Dialog();
        PojoView pojoView = SpringBeanFactory.create(PojoView.class);
        Div output = new Div(new Text("(no image file uploaded yet)"));

        String userIdentifier = (String) VaadinSession.getCurrent().getSession().getAttribute("USER_IDENTIFIER");
        String givenName = (String) VaadinSession.getCurrent().getSession().getAttribute("GIVENNAME");
        String familyName = (String) VaadinSession.getCurrent().getSession().getAttribute("FAMILYNAME");
        String email = (String) VaadinSession.getCurrent().getSession().getAttribute("EMAIL");
        Set<String> roles = (Set<String>) VaadinSession.getCurrent().getSession().getAttribute("ROLES");
        String csRoles = String.join(";", roles);
        String tenant = (String) VaadinSession.getCurrent().getSession().getAttribute("TENANT");

        FindAnyEntityQuery<UserProfile> userProfileQuery 
                = new FindAnyEntityQuery<UserProfile>(
                        UserProfile.class, 
                        dateFormat,
                        List.<UserProfile>of(),
                        Optional.of(
                                AndFilters.build(
                                        QueryFilter.build(
                                                "userIdentifier"
                                                , FilterRelation.EQUAL, 
                                                userIdentifier
                                        )
                                )
                        )
                );

        Collection<UserProfile> upDB = daoUserProfile.runQuery(userProfileQuery, Optional.of(tenant), Optional.empty());

        UserProfile userProfile = upDB.stream().findFirst().map(up -> {
            up.setEmail(email);
            up.setFirstName(givenName);
            up.setLastName(familyName);
            up.setRoles(csRoles);

            String path = "/" + profilePicBaseMinioDir + "/" + userIdentifier + ".png";
            if (minioService.exist(path)) {

                Image image = new Image(new StreamResource(userIdentifier + ".png", () -> {
                    return minioService.get(path);
                }), "Profile image");

                output.add(image);
            }
            return up;
        }).orElseGet(() -> {
            return (UserProfile) daoUserProfile.createAndSave(
                    UserProfile.class, 
                    Optional.of(tenant),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Status.DRAFT, 
                    userIdentifier).map(up2 -> {

                UserProfile up = (UserProfile) up2;
                up.setUserIdentifier(userIdentifier);
                up.setEmail(email);
                up.setFirstName(givenName);
                up.setLastName(familyName);
                up.setRoles(csRoles);
                daoUserProfile.save(up);
                return up;
            }).orElseGet(() -> new UserProfile());
        });

        pojoView.construct(userProfile, Optional.of(dialog2));

        Upload upload = new Upload(new LangkuikMultiFileBuffer());

        pojoView.add(upload, output);

        // Configure upload component
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFileSize(profilePicMaxUploadSize);

        upload.addSucceededListener(event -> {
            try {
                // String fileExtension = MimeTypeUtils.parseMimeType(event.getMIMEType()).getSubtype();
                InputStream is = ((LangkuikMultiFileBuffer) upload.getReceiver()).getInputStream(event.getFileName());
//                String tempDir = System.getProperty("java.io.tmpdir");
//                File file = new File(tempDir+"/"+userIdentifier+"."+fileExtension);
//                file.createNewFile();
//                FileUtils.copyInputStreamToFile(is, file);

                BufferedImage image = resizeImage(ImageIO.read(is), profilePicDimension, profilePicDimension);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(image, "png", os); //always save in png                         // Passing: ​(RenderedImage im, String formatName, OutputStream output)
                InputStream eis = new ByteArrayInputStream(os.toByteArray());
//                ImageIO.write(image, "png", file);//always saves in png

//                minioService.save(file, "/"+profilePicBaseMinioDir).ifPresent(relativePath -> {
//                    userProfile.setProfilePicURL(relativePath);
//                }); 
                minioService.save(eis, userIdentifier + ".png", "/" + profilePicBaseMinioDir).ifPresent(relativePath -> {
                    userProfile.setProfilePicURL(relativePath);
                });

                daoUserProfile.save(userProfile);

                output.removeAll();
                //output.add(new Text("Uploaded: " + originalFileName + " to " + file.getAbsolutePath() + "Type: " + mimeType));
                output.add(new Image(new StreamResource(userIdentifier + ".png", new InputStreamFactory() {
                    @Override
                    public InputStream createInputStream() {
                        try {
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            ImageIO.write(image, "png", os); //always save in png                         // Passing: ​(RenderedImage im, String formatName, OutputStream output)
                            return new ByteArrayInputStream(os.toByteArray());
                        } catch (IOException ex) {
                            Logger.getLogger(UserProfileDialogFactory.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;
                    }

                }), "Uploaded image"));
            } catch (IOException ex) {
                Logger.getLogger(UserProfileDialogFactory.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        upload.addFailedListener(event -> {
            output.removeAll();
            output.add(new Text("Upload failed: " + event.getReason()));
        });
        dialog2.add(pojoView);
        dialog2.open();
    }

    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

}

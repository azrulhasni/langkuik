/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.search;

import com.azrul.langkuik.framework.factory.SpringBeanFactory;
import com.azrul.langkuik.framework.minio.MinioService;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.extractor.mapping.annotation.ContainerExtraction;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.processing.PropertyMapping;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.processing.PropertyMappingAnnotationProcessor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.processing.PropertyMappingAnnotationProcessorContext;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.processing.PropertyMappingAnnotationProcessorRef;
import org.hibernate.search.mapper.pojo.mapping.definition.programmatic.PropertyMappingStep;

/**
 *
 * @author azrul
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ ElementType.METHOD, ElementType.FIELD }) 
@PropertyMapping(processor = @PropertyMappingAnnotationProcessorRef( 
        type = TikaField.Processor.class
))
@Documented 
@Repeatable(TikaField.List.class) 
public @interface TikaField {

    String name() default ""; 

    ContainerExtraction extraction() default @ContainerExtraction(); 

    @Documented
    @Target({ ElementType.METHOD, ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        TikaField[] value();
    }

    class Processor implements PropertyMappingAnnotationProcessor<TikaField> { 
        @Override
        public void process(PropertyMappingStep mapping, TikaField annotation,
                PropertyMappingAnnotationProcessorContext context) {
            
            MinioService minioService = SpringBeanFactory.create(MinioService.class);
            TikaBridge bridge = new TikaBridge(minioService);
            mapping.fullTextField(annotation.name().isEmpty() ? null : annotation.name())
                    .analyzer("english")
                    .searchable(Searchable.YES)
                    .valueBridge(bridge)
                    .extractors(context.toContainerExtractorPath(annotation.extraction())); 
        }
    }
}

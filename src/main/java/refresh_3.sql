/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  azrul
 * Created: 13 Jun 2022
 */

set session_replication_role to replica; --suspend all constraints

TRUNCATE TABLE address CASCADE;
TRUNCATE TABLE address_aud CASCADE;
TRUNCATE TABLE applicant CASCADE;
TRUNCATE TABLE applicant_aud CASCADE;
TRUNCATE TABLE application CASCADE;
TRUNCATE TABLE application_aud CASCADE;
TRUNCATE TABLE applications_applicants CASCADE;
TRUNCATE TABLE approval CASCADE;
TRUNCATE TABLE approval_aud CASCADE;
TRUNCATE TABLE attachment_aud CASCADE;
TRUNCATE TABLE attachment CASCADE;
TRUNCATE TABLE attachments CASCADE;
TRUNCATE TABLE attachmentscontainer CASCADE;
TRUNCATE TABLE collateral CASCADE;
TRUNCATE TABLE comment CASCADE;
TRUNCATE TABLE comment_aud CASCADE;
TRUNCATE TABLE comments CASCADE;
TRUNCATE TABLE commentscontainer CASCADE;
TRUNCATE TABLE revinfo CASCADE;
TRUNCATE TABLE workelement_approval_aud CASCADE;
TRUNCATE TABLE workelement_owners CASCADE;
TRUNCATE TABLE workelement_owners_aud CASCADE;
TRUNCATE TABLE application_collateral_aud CASCADE;
TRUNCATE TABLE applications_applicants_aud CASCADE;
TRUNCATE TABLE applications_applicants_aud CASCADE;


set session_replication_role to default; --put constraints back
--
-- PostgreSQL database dump
--

-- Dumped from database version 10.3
-- Dumped by pg_dump version 10.3

-- Started on 2021-08-15 19:21:20 +08



--
-- TOC entry 3190 (class 0 OID 95197)
-- Dependencies: 214

-- Data for Name: relationshipmanager; Type: TABLE DATA; Schema: public; Owner: LoanOrigSystem
--
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, forename, staffid, surname) VALUES (93, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2',  'END', NULL,'Alister', 'S1000001', 'McNeil');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, forename, staffid, surname) VALUES (96, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Jonathan', 'S1000004', 'Hollis');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, forename, staffid, surname) VALUES (95, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2',  'END',NULL,'Alexandra', 'S1000003', 'Cullen');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, forename, staffid, surname) VALUES (94, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END', NULL,'Connor', 'S1000002', 'Levine');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, forename, staffid, surname) VALUES (1107, 'cn=Gloria Nelson,ou=people,o=sevenSeas',2,NULL,NULL,'loanorigsystem2', 'END', NULL,'Harry', 'S1000011', 'Young');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, forename, staffid, surname) VALUES (1108, 'cn=Gloria Nelson,ou=people,o=sevenSeas',2,NULL,NULL,'loanorigsystem2', 'END', NULL,'Henry', 'S0000011', 'Young');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, forename, staffid, surname) VALUES (1109, 'cn=Gloria Nelson,ou=people,o=sevenSeas',2,NULL,NULL,'loanorigsystem2', 'END', NULL,'Henry', 'S1000011', 'Johnson');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, forename, staffid, surname) VALUES (1113, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2',  'END', NULL,'Henry', 'S1000011', 'Johnson');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, forename, staffid, surname) VALUES (1228, 'cn=Gloria Nelson,ou=people,o=sevenSeas',2,NULL,NULL,'loanorigsystem2',  'END', NULL,'Jimmy', 'S1000', 'Larson');
insert into public.workelement_owners (workelement_id, owners) VALUES (	93	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	94	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	95	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	96	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1107	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1108	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1109	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1113	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1228	,'cn=Gloria Nelson,ou=people,o=sevenSeas');

--
-- PostgreSQL database dump
--

-- Dumped from database version 10.3
-- Dumped by pg_dump version 10.3



--
-- TOC entry 3190 (class 0 OID 95166)
-- Dependencies: 209
-- Data for Name: districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, tranx_id, worklist, worklist_update_time, country, state, district); Type: TABLE DATA; Schema: public; Owner: LoanOrigSystem
--


INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1053, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Temerloh');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1054, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Penang', 'North-East Penang Island');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1055, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Penang', 'South-West Penang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1056, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Penang', 'North Seberang Perai');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1057, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Penang', 'Central Seberang Perai');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1058, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Penang', 'South Seberang Perai');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1059, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Batang Padang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1060, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Hilir Perak');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1061, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Hulu Perak');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1062, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Kerian');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1063, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Kinta');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1064, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Kuala Kangsar');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1065, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Larut, Matang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1066, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Selama');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1067, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Manjung');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1068, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Perak Tengah');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1069, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perak', 'Kampar');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1070, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Perlis', 'Perlis');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1071, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Selangor', 'Gombak');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1072, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Selangor', 'Hulu Langat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1073, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Selangor', 'Hulu Selangor');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1074, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Selangor', 'Klang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1075, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Selangor', 'Kuala Langat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1076, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Selangor', 'Kuala Selangor');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1077, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Selangor', 'Petaling');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1078, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Selangor', 'Sabak Bernam');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1079, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Selangor', 'Sepang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1080, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Terengganu', 'Besut');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1081, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Terengganu', 'Dungun');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1082, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Terengganu', 'Hulu Terengganu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1083, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Terengganu', 'Kemaman');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1084, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Terengganu', 'Kuala Terengganu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1085, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Terengganu', 'Marang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1086, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Terengganu', 'Setiu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1087, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Terengganu', 'Kuala Nerus');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1088, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Bukit Bintang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1089, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Bandar Tun Razak');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1090, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Cheras');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1091, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Setiawangsa');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1092, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Kepong');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1093, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Lembah Pantai');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1094, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Batu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1095, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Seputeh');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1096, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Segambut');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1097, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Titiwangsa');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1098, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Wangsa Maju');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1099, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Putrajaya', 'Putrajaya');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1100, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Beaufort');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1101, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Keningau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1102, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Kuala Penyu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1103, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Nabawan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1104, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Sipitang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1105, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Tambunan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1106, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Tenom');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1107, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Kota Marudu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1108, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Kudat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1109, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Pitas');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1110, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Beluran');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1111, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Kinabatangan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1112, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Sandakan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1113, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Tongod');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1114, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Kunak');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1115, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Lahad Datu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1002, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Johor', 'Johor Bahru');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1003, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Johor', 'Kluang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1004, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Johor', 'Kota Tinggi');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1005, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Johor', 'Mersing');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1006, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Johor', 'Muar');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1007, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Johor', 'Pontian');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1008, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Johor', 'Segamat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1009, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Johor', 'Kulaijaya');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1010, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Johor', 'Ledang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1011, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Baling');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1012, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Bandar Baharu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1013, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Kota Setar');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1014, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Kuala Muda');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1015, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Kubang Pasu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1016, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Kulim');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1017, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Pulau Langkawi');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1018, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Padang Terap');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1019, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Pendang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1020, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Pokok Sena');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1021, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Sik');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1022, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kedah', 'Yan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1023, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kelantan', 'Bachok');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1024, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kelantan', 'Gua Musang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1025, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kelantan', 'Jeli');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1026, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kelantan', 'Kota Bharu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1027, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kelantan', 'Kuala Krai');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1028, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kelantan', 'Machang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1029, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kelantan', 'Pasir Mas');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1030, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kelantan', 'Pasir Puteh');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1031, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kelantan', 'Tanah Merah');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1116, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Semporna');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1117, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Tawau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1118, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Kota Belud');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1119, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Kota Kinabalu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1120, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Papar');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1001, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Johor', 'Batu Pahat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1121, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Penampang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1122, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Putatan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1123, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Ranau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1124, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sabah', 'Tuaran');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1125, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Betong');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1126, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Saratok');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1127, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Bintulu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1128, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Tatau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1131, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Song');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1132, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Bau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1133, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Kuching');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1134, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Lundu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1135, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Lawas');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1136, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Limbang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1138, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Miri');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1139, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Dalat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1140, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Daro');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1141, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Matu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1142, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Mukah');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1143, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Asajaya');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1144, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Samarahan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1145, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Simunjan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1146, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Julau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1147, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Meradong');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1148, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Sarikei');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1149, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Pakan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1150, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Siburan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1032, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Kelantan', 'Tumpat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1033, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Malacca', 'Alor Gajah');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1034, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Malacca', 'Central Malacca');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1035, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Malacca', 'Jasin');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1036, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Negeri Sembilan', 'Jelebu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1037, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Negeri Sembilan', 'Jempol');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1038, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Negeri Sembilan', 'Kuala Pilah');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1039, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Negeri Sembilan', 'Port Dickson');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1040, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Negeri Sembilan', 'Rembau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1041, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Negeri Sembilan', 'Seremban');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1042, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Negeri Sembilan', 'Tampin');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1043, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Bentong');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1044, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Bera');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1045, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Cameron Highlands');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1046, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Jerantut');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1047, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Kuantan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1048, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Lipis');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1049, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Maran');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1050, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Pekan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1051, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Raub');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1052, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Pahang', 'Rompin');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1129, 'cn=Gloria Nelson,ou=people,o=sevenSeas',  2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Belaga');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1130, 'cn=Gloria Nelson,ou=people,o=sevenSeas',  2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Kapit');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1151, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Tebedu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1152, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Kanowit');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1153, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Sibu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1154, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Selangau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1155, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Lubok Antu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1156, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Sri Aman');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1157, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Federal Territory of Labuan', 'Labuan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, country, state, district) VALUES (1137, 'cn=Gloria Nelson,ou=people,o=sevenSeas',  2,NULL,NULL,'loanorigsystem2', 'END',NULL,'Malaysia', 'Sarawak', 'Marudi');

insert into public.workelement_owners (workelement_id, owners) VALUES (	1001	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1002	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1003	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1004	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1005	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1006	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1007	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1008	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1009	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1010	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1011	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1012	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1013	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1014	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1015	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1016	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1017	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1018	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1019	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1020	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1021	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1022	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1023	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1024	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1025	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1026	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1027	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1028	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1029	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1030	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1031	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1032	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1033	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1034	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1035	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1036	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1037	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1038	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1039	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1040	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1041	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1042	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1043	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1044	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1045	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1046	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1047	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1048	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1049	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1050	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1051	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1052	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1053	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1054	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1055	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1056	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1057	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1058	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1059	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1060	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1061	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1062	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1063	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1064	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1065	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1066	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1067	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1068	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1069	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1070	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1071	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1072	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1073	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1074	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1075	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1076	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1077	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1078	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1079	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1080	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1081	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1082	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1083	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1084	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1085	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1086	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1087	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1088	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1089	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1090	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1091	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1092	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1093	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1094	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1095	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1096	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1097	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1098	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1099	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1100	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1101	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1102	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1103	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1104	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1105	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1106	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1107	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1108	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1109	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1110	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1111	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1112	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1113	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1114	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1115	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1116	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1117	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1118	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1119	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1120	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1121	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1122	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1123	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1124	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1125	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1126	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1127	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1128	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1129	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1130	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1131	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1132	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1133	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1134	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1135	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1136	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1137	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1138	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1139	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1140	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1141	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1142	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1143	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1144	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1145	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1146	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1147	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1148	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1149	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1150	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1151	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1152	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1153	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1154	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1155	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1156	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1157	,'cn=Gloria Nelson,ou=people,o=sevenSeas');


--
-- TOC entry 3190 (class 0 OID 95191)
-- Dependencies: 213
-- Data for Name: productlisting; Type: TABLE DATA; Schema: public; Owner: LoanOrigSystem
--id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, product_name)

INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, product_name) VALUES (2002, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL, 'loanorigsystem2', 'END',NULL, 'CASH EXPRESS LOAN');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, product_name) VALUES (2004, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL, 'loanorigsystem2', 'END',NULL, 'HIRE PURCHASE');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, product_name) VALUES (2003, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL, 'loanorigsystem2', 'END',NULL, 'SME_LOAN');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, product_name) VALUES (2005, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL, 'loanorigsystem2', 'END',NULL, 'PERSONAL LOAN');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, product_name) VALUES (2001, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL, 'loanorigsystem2', 'END',NULL, 'HOME LOAN');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, product_name) VALUES (2006, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL, 'loanorigsystem2', 'END',NULL, 'HOME LOAN i');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, product_name) VALUES (1127, 'cn=Gloria Nelson,ou=people,o=sevenSeas',2,NULL,NULL, 'loanorigsystem2', 'END',NULL, 'ASNB');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, product_name) VALUES (1139, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL, 'loanorigsystem2', 'END',NULL, 'ANSB LOAN');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, product_name) VALUES (1138, 'cn=Gloria Nelson,ou=people,o=sevenSeas', 2,NULL,NULL, 'loanorigsystem2', 'END',NULL, 'ASNB LOAN-i');

insert into public.workelement_owners (workelement_id, owners) VALUES (	1127	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1138	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1139	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2001	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2002	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2003	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2004	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2005	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2006	,'cn=Gloria Nelson,ou=people,o=sevenSeas');
-- Completed on 2021-08-15 19:18:40 +08

--
-- PostgreSQL database dump complete
--



-- Completed on 2021-08-15 19:18:40 +08

--
-- PostgreSQL database dump complete
--


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
TRUNCATE TABLE collateral CASCADE;
TRUNCATE TABLE comment CASCADE;
TRUNCATE TABLE comment_aud CASCADE;
TRUNCATE TABLE comments CASCADE;
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
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, commentscollection_id, forename, staffid, surname) VALUES (93, 'glory.bee', 2,NULL,NULL, 'loanorigsystem',  'END', NULL,NULL,'Alister', 'S1000001', 'McNeil');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, commentscollection_id, forename, staffid, surname) VALUES (96, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', 'END',NULL,NULL, 'Jonathan', 'S1000004', 'Hollis');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, commentscollection_id, forename, staffid, surname) VALUES (95, 'glory.bee', 2,NULL,NULL, 'loanorigsystem',  'END',NULL,NULL, 'Alexandra', 'S1000003', 'Cullen');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, commentscollection_id, forename, staffid, surname) VALUES (94, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', 'END', NULL,NULL,'Connor', 'S1000002', 'Levine');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, commentscollection_id, forename, staffid, surname) VALUES (1107, 'glory.bee',2,NULL,NULL, 'loanorigsystem', 'END', NULL, NULL,'Harry', 'S1000011', 'Young');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, commentscollection_id, forename, staffid, surname) VALUES (1108, 'glory.bee',2,NULL,NULL, 'loanorigsystem', 'END', NULL, NULL,'Henry', 'S0000011', 'Young');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, commentscollection_id, forename, staffid, surname) VALUES (1109, 'glory.bee',2,NULL,NULL, 'loanorigsystem', 'END', NULL,NULL,'Henry', 'S1000011', 'Johnson');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, commentscollection_id, forename, staffid, surname) VALUES (1113, 'glory.bee', 2,NULL,NULL, 'loanorigsystem',  'END', NULL,NULL,'Henry', 'S1000011', 'Johnson');
INSERT INTO public.relationshipmanager(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,worklist, worklist_update_time, commentscollection_id, forename, staffid, surname) VALUES (1228, 'glory.bee',2,NULL,NULL, 'loanorigsystem',  'END', NULL,NULL,'Jimmy', 'S1000', 'Larson');
insert into public.workelement_owners (workelement_id, owners) VALUES (	93	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	94	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	95	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	96	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1107	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1108	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1109	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1113	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1228	,'glory.bee');

--
-- PostgreSQL database dump
--

-- Dumped from database version 10.3
-- Dumped by pg_dump version 10.3



--
-- TOC entry 3190 (class 0 OID 95166)
-- Dependencies: 209
-- Data for Name: districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, tranx_id, worklist, worklist_update_time, commentscollection_id, country, state, district); Type: TABLE DATA; Schema: public; Owner: LoanOrigSystem
--


INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1053, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Temerloh');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1054, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Penang', 'North-East Penang Island');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1055, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Penang', 'South-West Penang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1056, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Penang', 'North Seberang Perai');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1057, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Penang', 'Central Seberang Perai');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1058, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Penang', 'South Seberang Perai');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1059, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Batang Padang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1060, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Hilir Perak');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1061, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Hulu Perak');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1062, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Kerian');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1063, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Kinta');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1064, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Kuala Kangsar');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1065, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Larut, Matang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1066, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Selama');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1067, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Manjung');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1068, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Perak Tengah');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1069, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perak', 'Kampar');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1070, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Perlis', 'Perlis');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1071, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Selangor', 'Gombak');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1072, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Selangor', 'Hulu Langat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1073, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Selangor', 'Hulu Selangor');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1074, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Selangor', 'Klang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1075, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Selangor', 'Kuala Langat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1076, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Selangor', 'Kuala Selangor');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1077, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Selangor', 'Petaling');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1078, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Selangor', 'Sabak Bernam');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1079, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Selangor', 'Sepang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1080, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Terengganu', 'Besut');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1081, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Terengganu', 'Dungun');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1082, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Terengganu', 'Hulu Terengganu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1083, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Terengganu', 'Kemaman');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1084, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Terengganu', 'Kuala Terengganu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1085, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Terengganu', 'Marang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1086, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Terengganu', 'Setiu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1087, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Terengganu', 'Kuala Nerus');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1088, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Bukit Bintang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1089, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Bandar Tun Razak');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1090, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Cheras');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1091, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Setiawangsa');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1092, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Kepong');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1093, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Lembah Pantai');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1094, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Batu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1095, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Seputeh');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1096, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Segambut');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1097, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Titiwangsa');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1098, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Kuala Lumpur', 'Wangsa Maju');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1099, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Putrajaya', 'Putrajaya');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1100, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Beaufort');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1101, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Keningau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1102, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Kuala Penyu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1103, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Nabawan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1104, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Sipitang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1105, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Tambunan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1106, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Tenom');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1107, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Kota Marudu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1108, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Kudat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1109, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Pitas');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1110, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Beluran');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1111, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Kinabatangan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1112, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Sandakan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1113, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Tongod');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1114, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Kunak');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1115, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Lahad Datu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1002, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Johor', 'Johor Bahru');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1003, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Johor', 'Kluang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1004, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Johor', 'Kota Tinggi');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1005, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Johor', 'Mersing');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1006, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Johor', 'Muar');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1007, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Johor', 'Pontian');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1008, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Johor', 'Segamat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1009, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Johor', 'Kulaijaya');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1010, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Johor', 'Ledang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1011, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Baling');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1012, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Bandar Baharu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1013, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Kota Setar');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1014, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Kuala Muda');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1015, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Kubang Pasu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1016, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Kulim');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1017, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Pulau Langkawi');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1018, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Padang Terap');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1019, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Pendang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1020, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Pokok Sena');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1021, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Sik');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1022, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kedah', 'Yan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1023, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kelantan', 'Bachok');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1024, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kelantan', 'Gua Musang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1025, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kelantan', 'Jeli');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1026, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kelantan', 'Kota Bharu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1027, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kelantan', 'Kuala Krai');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1028, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kelantan', 'Machang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1029, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kelantan', 'Pasir Mas');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1030, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kelantan', 'Pasir Puteh');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1031, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kelantan', 'Tanah Merah');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1116, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Semporna');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1117, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Tawau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1118, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Kota Belud');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1119, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Kota Kinabalu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1120, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Papar');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1001, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Johor', 'Batu Pahat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1121, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Penampang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1122, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Putatan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1123, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Ranau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1124, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sabah', 'Tuaran');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1125, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Betong');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1126, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Saratok');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1127, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Bintulu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1128, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Tatau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1131, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Song');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1132, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Bau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1133, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Kuching');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1134, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Lundu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1135, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Lawas');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1136, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Limbang');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1138, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Miri');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1139, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Dalat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1140, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Daro');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1141, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Matu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1142, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Mukah');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1143, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Asajaya');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1144, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Samarahan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1145, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Simunjan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1146, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Julau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1147, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Meradong');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1148, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Sarikei');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1149, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Pakan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1150, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Siburan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1032, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Kelantan', 'Tumpat');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1033, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Malacca', 'Alor Gajah');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1034, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Malacca', 'Central Malacca');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1035, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Malacca', 'Jasin');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1036, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Negeri Sembilan', 'Jelebu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1037, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Negeri Sembilan', 'Jempol');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1038, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Negeri Sembilan', 'Kuala Pilah');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1039, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Negeri Sembilan', 'Port Dickson');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1040, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Negeri Sembilan', 'Rembau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1041, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Negeri Sembilan', 'Seremban');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1042, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Negeri Sembilan', 'Tampin');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1043, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Bentong');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1044, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Bera');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1045, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Cameron Highlands');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1046, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Jerantut');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1047, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Kuantan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1048, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Lipis');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1049, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Maran');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1050, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Pekan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1051, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Raub');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1052, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Pahang', 'Rompin');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1129, 'glory.bee',  2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Belaga');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1130, 'glory.bee',  2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Kapit');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1151, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Tebedu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1152, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Kanowit');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1153, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Sibu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1154, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Selangau');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1155, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Lubok Antu');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1156, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Sri Aman');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1157, 'glory.bee', 2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Federal Territory of Labuan', 'Labuan');
INSERT INTO public.districtstatecountry(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id,  worklist, worklist_update_time, commentscollection_id, country, state, district) VALUES (1137, 'glory.bee',  2,NULL,NULL,'loanorigsystem', 'END',NULL,NULL,'Malaysia', 'Sarawak', 'Marudi');

insert into public.workelement_owners (workelement_id, owners) VALUES (	1001	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1002	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1003	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1004	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1005	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1006	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1007	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1008	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1009	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1010	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1011	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1012	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1013	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1014	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1015	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1016	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1017	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1018	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1019	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1020	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1021	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1022	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1023	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1024	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1025	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1026	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1027	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1028	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1029	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1030	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1031	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1032	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1033	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1034	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1035	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1036	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1037	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1038	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1039	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1040	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1041	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1042	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1043	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1044	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1045	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1046	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1047	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1048	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1049	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1050	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1051	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1052	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1053	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1054	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1055	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1056	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1057	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1058	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1059	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1060	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1061	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1062	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1063	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1064	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1065	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1066	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1067	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1068	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1069	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1070	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1071	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1072	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1073	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1074	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1075	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1076	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1077	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1078	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1079	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1080	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1081	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1082	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1083	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1084	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1085	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1086	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1087	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1088	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1089	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1090	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1091	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1092	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1093	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1094	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1095	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1096	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1097	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1098	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1099	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1100	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1101	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1102	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1103	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1104	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1105	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1106	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1107	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1108	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1109	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1110	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1111	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1112	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1113	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1114	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1115	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1116	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1117	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1118	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1119	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1120	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1121	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1122	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1123	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1124	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1125	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1126	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1127	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1128	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1129	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1130	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1131	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1132	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1133	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1134	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1135	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1136	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1137	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1138	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1139	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1140	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1141	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1142	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1143	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1144	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1145	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1146	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1147	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1148	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1149	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1150	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1151	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1152	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1153	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1154	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1155	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1156	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1157	,'glory.bee');


--
-- TOC entry 3190 (class 0 OID 95191)
-- Dependencies: 213
-- Data for Name: productlisting; Type: TABLE DATA; Schema: public; Owner: LoanOrigSystem
--id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, commentscollection_id, product_name)

INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, commentscollection_id, product_name) VALUES (2002, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', 'END',NULL,NULL, 'CASH EXPRESS LOAN');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, commentscollection_id, product_name) VALUES (2004, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', 'END',NULL,NULL, 'HIRE PURCHASE');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, commentscollection_id, product_name) VALUES (2003, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', 'END',NULL,NULL, 'SME_LOAN');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, commentscollection_id, product_name) VALUES (2005, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', 'END',NULL,NULL, 'PERSONAL LOAN');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, commentscollection_id, product_name) VALUES (2001, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', 'END',NULL,NULL, 'HOME LOAN');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, commentscollection_id, product_name) VALUES (2006, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', 'END',NULL,NULL, 'HOME LOAN i');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, commentscollection_id, product_name) VALUES (1127, 'glory.bee',2,NULL,NULL, 'loanorigsystem', 'END',NULL,NULL, 'ASNB');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, commentscollection_id, product_name) VALUES (1139, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', 'END',NULL,NULL, 'ANSB LOAN');
INSERT INTO public.productlisting(id, creator_id, status, supervisor_approval_level, supervisor_approval_seeker, tenant_id, worklist, worklist_update_time, commentscollection_id, product_name) VALUES (1138, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', 'END',NULL,NULL, 'ASNB LOAN-i');

insert into public.workelement_owners (workelement_id, owners) VALUES (	1127	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1138	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	1139	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2001	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2002	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2003	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2004	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2005	,'glory.bee');
insert into public.workelement_owners (workelement_id, owners) VALUES (	2006	,'glory.bee');
-- Completed on 2021-08-15 19:18:40 +08

--
-- PostgreSQL database dump complete
--



-- Completed on 2021-08-15 19:18:40 +08

--
-- PostgreSQL database dump complete
--



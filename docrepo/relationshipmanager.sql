--
-- PostgreSQL database dump
--

-- Dumped from database version 10.3
-- Dumped by pg_dump version 10.3

-- Started on 2021-08-15 19:21:20 +08

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3190 (class 0 OID 95197)
-- Dependencies: 214
-- Data for Name: relationshipmanager; Type: TABLE DATA; Schema: public; Owner: LoanOrigSystem
--
﻿INSERT INTO public.relationshipmanager VALUES (93, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', '217cfde5-3af5-11eb-af89-b7643b9a0dd3', 'END', NULL,NULL,'Alister', 'S1000001', 'McNeil');
INSERT INTO public.relationshipmanager VALUES (96, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', '8aa9ca08-3afa-11eb-af89-d5e1f03fd274', 'END',NULL,NULL, 'Jonathan', 'S1000004', 'Hollis');
INSERT INTO public.relationshipmanager VALUES (95, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', '79cb1817-3afa-11eb-af89-c3a66ef7b249', 'END',NULL,NULL, 'Alexandra', 'S1000003', 'Cullen');
INSERT INTO public.relationshipmanager VALUES (94, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', '63580616-3afa-11eb-af89-75e0316e83da', 'END', NULL,NULL,'Connor', 'S1000002', 'Levine');
INSERT INTO public.relationshipmanager VALUES (1106, 'glory.bee',2,NULL,NULL, 'loanorigsystem', NULL, NULL, NULL,NULL,NULL, NULL, NULL);
INSERT INTO public.relationshipmanager VALUES (1107, 'glory.bee',2,NULL,NULL, 'loanorigsystem', NULL, NULL, NULL,NULL,'Harry', 'S1000011', 'Young');
INSERT INTO public.relationshipmanager VALUES (1108, 'glory.bee',2,NULL,NULL, 'loanorigsystem', NULL, NULL, NULL,NULL,'Henry', 'S0000011', 'Young');
INSERT INTO public.relationshipmanager VALUES (1110, 'glory.bee',2,NULL,NULL, 'loanorigsystem', NULL, NULL, NULL,NULL,NULL, NULL, NULL);
INSERT INTO public.relationshipmanager VALUES (1112, 'glory.bee',2,NULL,NULL, 'loanorigsystem', NULL, NULL, NULL,NULL,NULL, NULL, NULL);
INSERT INTO public.relationshipmanager VALUES (1111, 'glory.bee',2,NULL,NULL, 'loanorigsystem', NULL, NULL, NULL,NULL,NULL, NULL, NULL);
INSERT INTO public.relationshipmanager VALUES (1109, 'glory.bee',2,NULL,NULL, 'loanorigsystem', NULL, NULL, NULL,NULL,'Henry', 'S1000011', 'Johnson');
INSERT INTO public.relationshipmanager VALUES (1113, 'glory.bee', 2,NULL,NULL, 'loanorigsystem', NULL, 'END', NULL,NULL,'Henry', 'S1000011', 'Johnson');
INSERT INTO public.relationshipmanager VALUES (1228, 'glory.bee',2,NULL,NULL, 'loanorigsystem', NULL, 'END', NULL,NULL,'Jimmy', 'S1000', 'Larson');
﻿insert into public.work_owners (work_id, owners) VALUES (	93	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	94	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	95	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	96	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	1106	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	1107	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	1108	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	1109	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	1110	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	1111	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	1112	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	1113	,'glory.bee');
insert into public.work_owners (work_id, owners) VALUES (	1228	,'glory.bee');
-- Completed on 2021-08-15 19:21:20 +08

--
-- PostgreSQL database dump complete
--


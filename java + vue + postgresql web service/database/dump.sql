--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2023-06-10 10:56:08

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 3476 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 265 (class 1255 OID 16899)
-- Name: check_date_f(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.check_date_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$ 
declare
	prev_stationid integer;
	prev_date timestamp;
begin
	with
	MARSH as
	(select 
			marshrut.stationid, 
			order1
	from marshrut    
	inner join
		(select 
			marshrut.marshrut, 
			marshrut.stationid   
		from marshrut	
		inner join
			(select 
				m_num
			from trains
			where new.t_num = trains.t_num) as T
		on marshrut.m_num = T.m_num) as M
	on marshrut.marshrut = M.marshrut
	order by order1)
	select stationid into prev_stationid
	from MARSH
	inner join 
		(select order1
		from MARSH
		where stationid = new.stationid) as ORD
	on (new.napr and MARSH.order1 + 1 = ORD.order1) or (not new.napr and MARSH.order1 - 1= ORD.order1);
	
    select 
    	max(dt2) 
    into prev_date
    from timetable
    where 
    	t_num = new.t_num and 
    	napr = new.napr and 
    	stationid = prev_stationid;

    if (prev_date > new.dt1)
    then
    	begin
	    	raise 'В рейсе данного поезда отбытие с предыдущей станции произошло позже вреемени прибытия на данную';
    	end;
    end if;
    return new;

end;
$$;


ALTER FUNCTION public.check_date_f() OWNER TO postgres;

--
-- TOC entry 252 (class 1255 OID 16893)
-- Name: check_station_f(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.check_station_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$ 
begin
	if not exists(
		select 
			M.stationid 
		from marshrut    
		inner join
			(select 
				marshrut.marshrut, 
				marshrut.stationid   
			from marshrut	
			inner join
				(select 
					m_num
				from trains
				where new.t_num = trains.t_num) as T
			on marshrut.m_num = T.m_num) as M
		on marshrut.marshrut = M.marshrut and new.stationid = marshrut.stationid)
    then
    	begin
	    	raise 'В рейсе данного поезда нет такого маршрута';
    	end;
    end if;
    return new;
end;
$$;


ALTER FUNCTION public.check_station_f() OWNER TO postgres;

--
-- TOC entry 267 (class 1255 OID 25221)
-- Name: correct_timetable(date); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.correct_timetable(er date) RETURNS void
    LANGUAGE plpgsql
    AS $$
begin
	update timetable 
	set(dt1, dt2) = (dt1 + value, dt2 + value) 
	from 
		(select
			timetable.id as tt_id,
			value 
		from
			(select
				t_num,
				dt,
				napr,
				value
			from waitings
			where dt::date = er) as W
		inner join timetable
		on timetable.t_num = W.t_num and
			timetable.napr = W.napr and
			dt - value < dt1 and dt1 < dt - value + '2 days') as TT
	where tt_id = timetable.id;

	update waitings 
	set value = '0' 
	from 
		(select
			id
		from waitings
		where dt::date = er) as W
	where W.id = waitings.id;
end;
$$;


ALTER FUNCTION public.correct_timetable(er date) OWNER TO postgres;

--
-- TOC entry 256 (class 1255 OID 33759)
-- Name: getautolist(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getautolist() RETURNS TABLE(transportid integer, type character varying, place character varying, dateofpurchase date, workinglife interval)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT tr.id,
               tp.name,
               p.name,
               tr.dt,
               tr.working_life
        FROM transport AS tr
                 INNER JOIN types AS tp ON tr.typeId = tp.typeId
                 INNER JOIN places AS p ON p.placeId = tr.placeId;
END;
$$;


ALTER FUNCTION public.getautolist() OWNER TO postgres;

--
-- TOC entry 263 (class 1255 OID 33786)
-- Name: getautoplaceinfo(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getautoplaceinfo() RETURNS TABLE(transportid integer, place character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT 
               tr.Id,
               p.name
        FROM transport AS tr
                 INNER JOIN places AS p ON p.placeId = tr.placeId
        ORDER BY tr.Id;
END;
$$;


ALTER FUNCTION public.getautoplaceinfo() OWNER TO postgres;

--
-- TOC entry 260 (class 1255 OID 33788)
-- Name: getcargotransportation(integer[], character varying[], date[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getcargotransportation(autoid integer[], typename character varying[], datelist date[]) RETURNS TABLE(cargotransportation bigint)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT sum(dr.cargo_transportation)
        FROM types 
                 INNER JOIN transport AS tr ON types.typeId = tr.typeId
                 INNER JOIN daily_reports AS dr ON dr.transportId = tr.Id
        WHERE (((cardinality(autoId) != 0 AND dr.transportId = ANY (autoId)) OR cardinality(autoId) = 0) AND
              ((cardinality(typeName) != 0 AND types.name = ANY (typeName)) OR cardinality(typeName) = 0)) AND
              ((cardinality(dateList) != 0 AND dr.report_date BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) OR 
              cardinality(dateList) = 0);
END;
$$;


ALTER FUNCTION public.getcargotransportation(autoid integer[], typename character varying[], datelist date[]) OWNER TO postgres;

--
-- TOC entry 269 (class 1255 OID 33839)
-- Name: getdecommissionedandpurchased(date[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getdecommissionedandpurchased(datelist date[]) RETURNS TABLE(transportid integer, type character varying, place character varying, dateofpurchase date, workinglife interval, decommission boolean, purchased boolean)
    LANGUAGE plpgsql
    AS $$
    BEGIN
        RETURN QUERY
        SELECT tr.id,
               tp.name,
               p.name,
               tr.dt,
               tr.working_life,
               (tr.dt BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) OR cardinality(dateList) = 0,
               ((tr.dt + tr.working_life) BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) OR (cardinality(dateList) = 0 AND (NOW()::DATE > tr.dt + tr.working_life))
        FROM transport AS tr
            INNER JOIN types AS tp ON tr.typeId = tp.typeId
            INNER JOIN places AS p ON p.placeId = tr.placeId
        WHERE (cardinality(dateList) != 0 AND (tr.dt BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) OR 
                                             ((tr.dt + tr.working_life) BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)))
          OR   cardinality(dateList) = 0 
        ORDER BY dateOfPurchase;
    END;
    $$;


ALTER FUNCTION public.getdecommissionedandpurchased(datelist date[]) OWNER TO postgres;

--
-- TOC entry 255 (class 1255 OID 33763)
-- Name: getdriverlist(integer[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getdriverlist(autoid integer[]) RETURNS TABLE(emplid integer, name character varying, transportid integer, typename character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT e.emplId,
               e.FIO,
               te.transportId,
               types.name
        FROM empl AS e
                 INNER JOIN transport_empl AS te ON e.emplId = te.emplId AND e.postId = 1
                 INNER JOIN transport AS t ON t.Id = te.transportId
                 INNER JOIN types ON types.typeId = t.typeId
        WHERE ((cardinality(autoId) != 0 AND te.transportId = ANY (autoId)) OR cardinality(autoId) = 0)
        order by te.transportId;
END;
$$;


ALTER FUNCTION public.getdriverlist(autoid integer[]) OWNER TO postgres;

--
-- TOC entry 254 (class 1255 OID 33742)
-- Name: getenddate(anyarray); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getenddate(anyarray) RETURNS anyelement
    LANGUAGE sql
    AS $_$
SELECT max(elements)
FROM unnest($1) elements
$_$;


ALTER FUNCTION public.getenddate(anyarray) OWNER TO postgres;

--
-- TOC entry 258 (class 1255 OID 33768)
-- Name: getmileage(integer[], character varying[], date[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getmileage(autoid integer[], typename character varying[], datelist date[]) RETURNS TABLE(mileage bigint)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT sum(dr.mileage)
        FROM types 
                 INNER JOIN transport AS tr ON types.typeId = tr.typeId
                 INNER JOIN daily_reports AS dr ON dr.transportId = tr.Id
        WHERE (((cardinality(autoId) != 0 AND dr.transportId = ANY (autoId)) OR cardinality(autoId) = 0) AND
              ((cardinality(typeName) != 0 AND types.name = ANY (typeName)) OR cardinality(typeName) = 0)) AND
              ((cardinality(dateList) != 0 AND dr.report_date BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) OR 
              cardinality(dateList) = 0);
END;
$$;


ALTER FUNCTION public.getmileage(autoid integer[], typename character varying[], datelist date[]) OWNER TO postgres;

--
-- TOC entry 262 (class 1255 OID 33782)
-- Name: getplaceinfo(character varying[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getplaceinfo(typename character varying[]) RETURNS TABLE(place character varying, type character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT distinct p.name,
               types.name
        FROM transport AS tr
                 INNER JOIN types ON tr.typeId = types.typeId
                 INNER JOIN places AS p ON p.placeId = tr.placeId
        WHERE ((cardinality(typeName) != 0 AND types.name = ANY (typeName)) OR cardinality(typeName) = 0)
        ORDER BY p.name;
END;
$$;


ALTER FUNCTION public.getplaceinfo(typename character varying[]) OWNER TO postgres;

--
-- TOC entry 261 (class 1255 OID 33780)
-- Name: getpostinfo(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getpostinfo() RETURNS TABLE(fio character varying, post character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT e.FIO,
               p.name
        FROM empl AS e
            INNER JOIN posts AS p ON p.postId = e.postId
        ORDER BY name;
END;
$$;


ALTER FUNCTION public.getpostinfo() OWNER TO postgres;

--
-- TOC entry 259 (class 1255 OID 33774)
-- Name: getrepaircountandcost(integer[], character varying[], date[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getrepaircountandcost(autoid integer[], typename character varying[], datelist date[]) RETURNS TABLE(countofrepairs bigint, costofrepairs bigint)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT 
            count(rr.rrid),
            sum(rr.cost)
        FROM types 
                 INNER JOIN transport AS tr ON types.typeId = tr.typeId
                 INNER JOIN repair_reports AS rr ON rr.transportId = tr.Id
        WHERE (((cardinality(autoId) != 0 AND rr.transportId = ANY (autoId)) OR cardinality(autoId) = 0) AND
              ((cardinality(typeName) != 0 AND types.name = ANY (typeName)) OR cardinality(typeName) = 0)) AND
              ((cardinality(dateList) != 0 AND (rr.date_of_receipt BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) AND 
                                               (rr.completion_date BETWEEN GetStartDate(dateList) AND GetEndDate(dateList))) OR 
              cardinality(dateList) = 0);
END;
$$;


ALTER FUNCTION public.getrepaircountandcost(autoid integer[], typename character varying[], datelist date[]) OWNER TO postgres;

--
-- TOC entry 271 (class 1255 OID 33850)
-- Name: getrepairinfo(integer[], date[], integer[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getrepairinfo(personid integer[], datelist date[], tid integer[]) RETURNS TABLE(dateofreceipt date, completiondate date, transportidentifier integer, worktype character varying, costofrepair integer, emplidentifier integer, name character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT date_of_receipt,
               completion_date,
               transportId,
               work_type,
               cost,
               rr.emplId,
               FIO
        FROM repair_reports AS rr
            INNER JOIN empl AS e ON e.emplId = rr.emplId
        WHERE ((cardinality(personId) != 0 AND rr.emplId = ANY (personId)) OR cardinality(personId) = 0)
          AND ((cardinality(tId) != 0 AND transportId = ANY (tId)) OR cardinality(tId) = 0)
          AND ((cardinality(dateList) != 0 AND (date_of_receipt BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) AND 
                                               (completion_date BETWEEN GetStartDate(dateList) AND GetEndDate(dateList))) OR 
              cardinality(dateList) = 0)
        ORDER BY date_of_receipt;
END;
$$;


ALTER FUNCTION public.getrepairinfo(personid integer[], datelist date[], tid integer[]) OWNER TO postgres;

--
-- TOC entry 268 (class 1255 OID 33794)
-- Name: getrepairpartsinfo(character varying[], integer[], character varying[], date[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getrepairpartsinfo(partslist character varying[], autoid integer[], tname character varying[], datelist date[]) RETURNS TABLE(partname character varying, transportid integer, typename character varying, countofparts integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT 
            p.name,
            tr.Id,
            t.name,
            rp.count
        FROM types 
                 INNER JOIN transport AS tr ON types.typeId = tr.typeId
                 INNER JOIN repair_reports AS rr ON rr.transportId = tr.Id
                 INNER JOIN repair_parts AS rp ON rr.rrId = rr.rrId
                 INNER JOIN parts AS p ON p.partId = rp.partId
                 INNER JOIN types AS t ON t.typeId = tr.typeId
        WHERE (((cardinality(autoId) != 0 AND rr.transportId = ANY (autoId)) OR cardinality(autoId) = 0) AND
              ((cardinality(tName) != 0 AND types.name = ANY (tName)) OR cardinality(tName) = 0)) AND
              ((cardinality(partsList) != 0 AND p.name = ANY (partsList)) OR cardinality(partsList) = 0) AND
              ((cardinality(dateList) != 0 AND (rr.date_of_receipt BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) AND 
                                               (rr.completion_date BETWEEN GetStartDate(dateList) AND GetEndDate(dateList))) OR 
              cardinality(dateList) = 0)
        ORDER BY p.name;
END;
$$;


ALTER FUNCTION public.getrepairpartsinfo(partslist character varying[], autoid integer[], tname character varying[], datelist date[]) OWNER TO postgres;

--
-- TOC entry 257 (class 1255 OID 33764)
-- Name: getschedule(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getschedule() RETURNS TABLE(nameofmarshrut character varying, transportid integer)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT m.name,
               sh.transportId
        FROM schedules AS sh
                 INNER JOIN marshs AS m ON m.marshrutId = sh.marshrutId;
END;
$$;


ALTER FUNCTION public.getschedule() OWNER TO postgres;

--
-- TOC entry 253 (class 1255 OID 33741)
-- Name: getstartdate(anyarray); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getstartdate(anyarray) RETURNS anyelement
    LANGUAGE sql
    AS $_$
SELECT min(elements)
FROM unnest($1) elements
$_$;


ALTER FUNCTION public.getstartdate(anyarray) OWNER TO postgres;

--
-- TOC entry 270 (class 1255 OID 33841)
-- Name: getsubordinates(integer[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.getsubordinates(personid integer[]) RETURNS TABLE(id integer, fio character varying, post character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
        SELECT 
               e.emplId,
               e.FIO,
               p.name
        FROM empl AS e
                 INNER JOIN posts AS p ON e.postId = p.postId
        WHERE ((cardinality(personId) != 0 AND e.headId = ANY (personId)) OR cardinality(personId) = 0)
        ORDER BY p.name;
END;
$$;


ALTER FUNCTION public.getsubordinates(personid integer[]) OWNER TO postgres;

--
-- TOC entry 251 (class 1255 OID 16847)
-- Name: log_del_f(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.log_del_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
    if old.tickets > 300 and exists(select id from log_timetable where old.id = id)
    then
    	delete from log_timetable 
    		where old.id = id;
	end if;
	return new;
end;
$$;


ALTER FUNCTION public.log_del_f() OWNER TO postgres;

--
-- TOC entry 264 (class 1255 OID 16844)
-- Name: log_ins_f(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.log_ins_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
    if new.tickets > 300 and not exists(select id from log_timetable where new.id = id)
    then
    	insert into log_timetable values(new.*);
	end if;
	return new;
end;
$$;


ALTER FUNCTION public.log_ins_f() OWNER TO postgres;

--
-- TOC entry 266 (class 1255 OID 24995)
-- Name: without_id_f(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.without_id_f() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
declare 
	min_marsh integer;
begin
    if new.m_num is null
    then
    	if new.marshrut is null
	    then
			begin
				select 
					min(rnum) into min_marsh
				from
					(select 
						marshrut, 
						row_number() over(order by marshrut) as rnum 
					from 
						(select 
							distinct marshrut 
						from marshrut) as M1) as M2
					where rnum != marshrut;
				if(min_marsh is not null)
				then
					new.marshrut = min_marsh;
				else
					select 
						max(marshrut) + 1 into min_marsh
					from marshrut;
				end if;
				new.m_num := min_marsh * 100 + new.order1;
				new.marshrut := min_marsh;
			end;
    	else
	    	begin
	    		if exists(select marshrut from marshrut where new.marshrut = marshrut and new.order1 = order1)
	    		then
	    			raise 'Элемент с таким значением порядка уже находится в таблице';
	    		end if;
	    		new.m_num := new.marshrut * 100 + new.order1;
	    	end;
    	end if;
    end if;
    return new;
end;
$$;


ALTER FUNCTION public.without_id_f() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 237 (class 1259 OID 33714)
-- Name: daily_reports; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.daily_reports (
    drid integer NOT NULL,
    transportid integer NOT NULL,
    report_date date NOT NULL,
    tickets_count integer NOT NULL,
    mileage integer NOT NULL,
    cargo_transportation integer NOT NULL,
    CONSTRAINT daily_reports_tickets_count_check CHECK ((tickets_count >= 0))
);


ALTER TABLE public.daily_reports OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 33713)
-- Name: daily_reports_drid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.daily_reports_drid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.daily_reports_drid_seq OWNER TO postgres;

--
-- TOC entry 3477 (class 0 OID 0)
-- Dependencies: 236
-- Name: daily_reports_drid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.daily_reports_drid_seq OWNED BY public.daily_reports.drid;


--
-- TOC entry 230 (class 1259 OID 33643)
-- Name: empl; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.empl (
    emplid integer NOT NULL,
    fio character varying(50) NOT NULL,
    postid integer NOT NULL,
    headid integer NOT NULL,
    CONSTRAINT empl_headid_check CHECK ((headid >= 0))
);


ALTER TABLE public.empl OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 33642)
-- Name: empl_emplid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.empl_emplid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.empl_emplid_seq OWNER TO postgres;

--
-- TOC entry 3478 (class 0 OID 0)
-- Dependencies: 229
-- Name: empl_emplid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.empl_emplid_seq OWNED BY public.empl.emplid;


--
-- TOC entry 219 (class 1259 OID 33573)
-- Name: marshs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.marshs (
    marshrutid integer NOT NULL,
    name character varying(40) NOT NULL
);


ALTER TABLE public.marshs OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 33572)
-- Name: marshs_marshrutid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.marshs_marshrutid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.marshs_marshrutid_seq OWNER TO postgres;

--
-- TOC entry 3479 (class 0 OID 0)
-- Dependencies: 218
-- Name: marshs_marshrutid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.marshs_marshrutid_seq OWNED BY public.marshs.marshrutid;


--
-- TOC entry 223 (class 1259 OID 33587)
-- Name: parts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parts (
    partid integer NOT NULL,
    name character varying(40) NOT NULL
);


ALTER TABLE public.parts OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 33586)
-- Name: parts_partid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.parts_partid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.parts_partid_seq OWNER TO postgres;

--
-- TOC entry 3480 (class 0 OID 0)
-- Dependencies: 222
-- Name: parts_partid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.parts_partid_seq OWNED BY public.parts.partid;


--
-- TOC entry 225 (class 1259 OID 33594)
-- Name: places; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.places (
    placeid integer NOT NULL,
    name character varying(40) NOT NULL
);


ALTER TABLE public.places OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 33593)
-- Name: places_placeid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.places_placeid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.places_placeid_seq OWNER TO postgres;

--
-- TOC entry 3481 (class 0 OID 0)
-- Dependencies: 224
-- Name: places_placeid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.places_placeid_seq OWNED BY public.places.placeid;


--
-- TOC entry 221 (class 1259 OID 33580)
-- Name: posts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.posts (
    postid integer NOT NULL,
    name character varying(30) NOT NULL
);


ALTER TABLE public.posts OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 33579)
-- Name: posts_postid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.posts_postid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.posts_postid_seq OWNER TO postgres;

--
-- TOC entry 3482 (class 0 OID 0)
-- Dependencies: 220
-- Name: posts_postid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.posts_postid_seq OWNED BY public.posts.postid;


--
-- TOC entry 234 (class 1259 OID 33686)
-- Name: repair_parts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.repair_parts (
    rrid integer NOT NULL,
    partid integer NOT NULL,
    count integer NOT NULL,
    CONSTRAINT repair_parts_count_check CHECK ((count > 0))
);


ALTER TABLE public.repair_parts OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 33669)
-- Name: repair_reports; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.repair_reports (
    rrid integer NOT NULL,
    date_of_receipt date NOT NULL,
    completion_date date NOT NULL,
    transportid integer NOT NULL,
    work_type character varying(200) NOT NULL,
    cost integer NOT NULL,
    emplid integer NOT NULL,
    CONSTRAINT repair_reports_cost_check CHECK ((cost >= 0))
);


ALTER TABLE public.repair_reports OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 33668)
-- Name: repair_reports_rrid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.repair_reports_rrid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.repair_reports_rrid_seq OWNER TO postgres;

--
-- TOC entry 3483 (class 0 OID 0)
-- Dependencies: 232
-- Name: repair_reports_rrid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.repair_reports_rrid_seq OWNED BY public.repair_reports.rrid;


--
-- TOC entry 235 (class 1259 OID 33700)
-- Name: schedules; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.schedules (
    transportid integer NOT NULL,
    marshrutid integer NOT NULL
);


ALTER TABLE public.schedules OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 33601)
-- Name: transport; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transport (
    id integer NOT NULL,
    typeid integer NOT NULL,
    placeid integer NOT NULL,
    dt date NOT NULL,
    working_life interval NOT NULL
);


ALTER TABLE public.transport OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 33655)
-- Name: transport_empl; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transport_empl (
    transportid integer NOT NULL,
    emplid integer NOT NULL
);


ALTER TABLE public.transport_empl OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 33600)
-- Name: transport_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transport_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transport_id_seq OWNER TO postgres;

--
-- TOC entry 3484 (class 0 OID 0)
-- Dependencies: 226
-- Name: transport_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transport_id_seq OWNED BY public.transport.id;


--
-- TOC entry 239 (class 1259 OID 33727)
-- Name: type_properties; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.type_properties (
    propid integer NOT NULL,
    typeid integer NOT NULL,
    name character varying(40) NOT NULL
);


ALTER TABLE public.type_properties OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 33726)
-- Name: type_properties_propid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.type_properties_propid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.type_properties_propid_seq OWNER TO postgres;

--
-- TOC entry 3485 (class 0 OID 0)
-- Dependencies: 238
-- Name: type_properties_propid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.type_properties_propid_seq OWNED BY public.type_properties.propid;


--
-- TOC entry 228 (class 1259 OID 33629)
-- Name: type_value; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.type_value (
    transportid integer NOT NULL,
    propid integer NOT NULL,
    value integer NOT NULL
);


ALTER TABLE public.type_value OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 33566)
-- Name: types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.types (
    typeid integer NOT NULL,
    name character varying(20) NOT NULL
);


ALTER TABLE public.types OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 33565)
-- Name: types_typeid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.types_typeid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.types_typeid_seq OWNER TO postgres;

--
-- TOC entry 3486 (class 0 OID 0)
-- Dependencies: 216
-- Name: types_typeid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.types_typeid_seq OWNED BY public.types.typeid;


--
-- TOC entry 3265 (class 2604 OID 33717)
-- Name: daily_reports drid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_reports ALTER COLUMN drid SET DEFAULT nextval('public.daily_reports_drid_seq'::regclass);


--
-- TOC entry 3263 (class 2604 OID 33646)
-- Name: empl emplid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empl ALTER COLUMN emplid SET DEFAULT nextval('public.empl_emplid_seq'::regclass);


--
-- TOC entry 3258 (class 2604 OID 33576)
-- Name: marshs marshrutid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.marshs ALTER COLUMN marshrutid SET DEFAULT nextval('public.marshs_marshrutid_seq'::regclass);


--
-- TOC entry 3260 (class 2604 OID 33590)
-- Name: parts partid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parts ALTER COLUMN partid SET DEFAULT nextval('public.parts_partid_seq'::regclass);


--
-- TOC entry 3261 (class 2604 OID 33597)
-- Name: places placeid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.places ALTER COLUMN placeid SET DEFAULT nextval('public.places_placeid_seq'::regclass);


--
-- TOC entry 3259 (class 2604 OID 33583)
-- Name: posts postid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts ALTER COLUMN postid SET DEFAULT nextval('public.posts_postid_seq'::regclass);


--
-- TOC entry 3264 (class 2604 OID 33672)
-- Name: repair_reports rrid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.repair_reports ALTER COLUMN rrid SET DEFAULT nextval('public.repair_reports_rrid_seq'::regclass);


--
-- TOC entry 3262 (class 2604 OID 33604)
-- Name: transport id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transport ALTER COLUMN id SET DEFAULT nextval('public.transport_id_seq'::regclass);


--
-- TOC entry 3266 (class 2604 OID 33730)
-- Name: type_properties propid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.type_properties ALTER COLUMN propid SET DEFAULT nextval('public.type_properties_propid_seq'::regclass);


--
-- TOC entry 3257 (class 2604 OID 33569)
-- Name: types typeid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.types ALTER COLUMN typeid SET DEFAULT nextval('public.types_typeid_seq'::regclass);


--
-- TOC entry 3468 (class 0 OID 33714)
-- Dependencies: 237
-- Data for Name: daily_reports; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.daily_reports (drid, transportid, report_date, tickets_count, mileage, cargo_transportation) FROM stdin;
1	1	2024-05-15	100	70	17
2	1	2025-05-15	150	70	35
\.


--
-- TOC entry 3461 (class 0 OID 33643)
-- Dependencies: 230
-- Data for Name: empl; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.empl (emplid, fio, postid, headid) FROM stdin;
1	Расторгуев\tЕфим\tЛаврентиич	4	0
2	Вергун\tДаниил\tГаврннлович	1	5
3	Лоскутников\tМаксим\tЛеонидович	2	5
4	Вотяков\tНикита\tДавидович	3	5
5	Берия\tВенедикт\tЮрьевич	5	1
6	Муравей\tСергей\tСаввеевич	6	5
7	Жолдин\tЕмельян\tКириллович	7	5
8	Хуртин\tПрохор\tКириллович	8	5
9	Назаров\tВасилий\tФеоктистович	9	5
10	Журбин\tКонстантин\tКонстантинович	10	5
11	Жиганов\tАлексей\tГеоргиевич	11	5
12	Платонов\tТимофей\tВасильевич	5	1
13	Никитаев\tТрофим\tФадеевич	1	12
14	Кузинов\tПавел\tГерманович	2	12
15	Суворов\tИлья\tСеменович	3	12
16	Эскин\tИгнат\tСеменович	6	12
17	Яндукин\tЛев\tЮрьевич	7	12
18	Ямлиханов\tФеликс\tВалерьевич	8	12
19	Сухих\tГерман\tЕгорович	9	12
20	Кутлыев\tРостислав\tВитальевич	10	12
21	Иканов\tКирилл\tСтепанович	11	12
22	Ядрышников\tИлья\tЕфимович	5	1
23	Корепанов\tТимофей\tФилиппович	5	1
24	Яманов\tВиталий\tЮлианович	5	1
25	Беломестов\tВалерий\tСтепанович	1	22
26	Смирновский\tКирилл\tГаврннлович	1	23
27	Половов\tЮрий\tВасильевич	1	24
28	Смольянинов\tЕмельян\tФедорович	1	22
29	Ядрихинский\tВиктор\tГерманович	1	23
30	Ямзин\tГеоргий\tПрохорович	1	24
31	Баранов\tСавва\tПавлович	1	22
32	Циглер\tВасилий\tАнтонович	1	23
33	Крутов\tЕвгений\tДенисович	1	24
34	Галыгин\tТрофим\tКириллович	1	22
35	Кумовьев\tСавва\tФедорович	1	23
36	Турчанинов\tСемен\tКонстантинович	1	24
37	Шемякин\tАнтон\tИгнатьевич	1	22
38	Верясов\tЕфрем\tИльич	1	23
39	Воскобойников\tДавид\tГригорьевич	1	24
40	Сиянин\tНиколай\tЛьвович	1	22
\.


--
-- TOC entry 3450 (class 0 OID 33573)
-- Dependencies: 219
-- Data for Name: marshs; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.marshs (marshrutid, name) FROM stdin;
1	Верх-Тула → Центральный корпус
2	посёлок Краснообск → Аэропорт Толмачёво
3	пос. Краснозёрское → Новосибирск
4	Новосибирск → Чистоозерное
5	посёлок Ордынское → Новосибирск
6	Кемерово → Аэропорт "Толмачёво"
7	Метро "Заельцовская" → Поселок Колывань
8	Кисловодск → Новокузнецк
9	Тюмень  → Новосибирск
10	Новокузнецк → станция Адлер
\.


--
-- TOC entry 3454 (class 0 OID 33587)
-- Dependencies: 223
-- Data for Name: parts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parts (partid, name) FROM stdin;
1	двигатель
2	трансмиссионная система
3	ходовая часть
4	кузов
5	рулевое управление
6	тормозная система
7	электрооборудование
\.


--
-- TOC entry 3456 (class 0 OID 33594)
-- Dependencies: 225
-- Data for Name: places; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.places (placeid, name) FROM stdin;
1	гараж 123
2	гараж 223
3	гараж 321
4	цех 222
5	цех 321
6	цех 432
7	бокс 221
8	бокс 321
9	бокс 223
\.


--
-- TOC entry 3452 (class 0 OID 33580)
-- Dependencies: 221
-- Data for Name: posts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.posts (postid, name) FROM stdin;
1	водитель
2	техник
3	кондуктор
4	глава предприятия
5	глава участка
6	слесарь
7	сварщик
8	сборщик
9	рабочий
10	бригадир
11	мастер
\.


--
-- TOC entry 3465 (class 0 OID 33686)
-- Dependencies: 234
-- Data for Name: repair_parts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.repair_parts (rrid, partid, count) FROM stdin;
1	1	1
1	2	1
2	3	2
2	4	1
3	5	2
4	6	1
\.


--
-- TOC entry 3464 (class 0 OID 33669)
-- Dependencies: 233
-- Data for Name: repair_reports; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.repair_reports (rrid, date_of_receipt, completion_date, transportid, work_type, cost, emplid) FROM stdin;
1	2023-04-05	2023-05-15	1	бла бла бла 1	10040	3
2	2023-04-13	2023-04-18	1	бла бла бла 2	20040	6
3	2023-04-17	2023-05-12	1	бла бла бла 3	15040	14
4	2023-04-15	2023-05-25	1	бла бла бла 4	15040	16
\.


--
-- TOC entry 3466 (class 0 OID 33700)
-- Dependencies: 235
-- Data for Name: schedules; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.schedules (transportid, marshrutid) FROM stdin;
1	1
2	2
3	3
4	4
5	5
6	6
7	7
8	8
9	9
10	10
11	1
12	2
13	3
14	4
15	5
16	6
\.


--
-- TOC entry 3458 (class 0 OID 33601)
-- Dependencies: 227
-- Data for Name: transport; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transport (id, typeid, placeid, dt, working_life) FROM stdin;
1	1	1	2020-07-26	10 years
2	1	1	2016-02-16	10 years
3	1	2	2017-11-23	10 years
4	1	3	2019-02-26	10 years
5	2	2	2021-07-26	10 years
6	2	2	2015-02-16	10 years
7	2	3	2016-11-23	10 years
8	2	1	2018-02-26	10 years
9	3	2	2021-07-26	10 years
10	3	2	2015-02-16	10 years
11	3	3	2016-11-23	10 years
12	3	3	2018-02-26	10 years
13	4	1	2021-07-26	10 years
14	4	2	2015-02-16	10 years
15	4	3	2016-11-23	10 years
16	4	1	2018-02-26	10 years
\.


--
-- TOC entry 3462 (class 0 OID 33655)
-- Dependencies: 231
-- Data for Name: transport_empl; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transport_empl (transportid, emplid) FROM stdin;
1	2
1	4
2	13
2	15
3	25
4	26
5	27
6	28
7	29
8	30
9	31
10	32
11	33
12	34
13	35
14	36
15	37
16	38
\.


--
-- TOC entry 3470 (class 0 OID 33727)
-- Dependencies: 239
-- Data for Name: type_properties; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.type_properties (propid, typeid, name) FROM stdin;
1	1	вместимость
2	1	максимальная скорость
3	2	вместимость
4	2	грузоподъемность
5	2	максимальная скорость
6	3	грузоподъемность
7	3	вместимость
8	3	максимальная скорость
9	4	вместимость
10	4	максимальная скорость
\.


--
-- TOC entry 3459 (class 0 OID 33629)
-- Dependencies: 228
-- Data for Name: type_value; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.type_value (transportid, propid, value) FROM stdin;
1	1	100
1	2	120
2	1	90
2	2	110
3	1	90
3	2	120
4	1	100
4	2	110
5	3	100
5	4	100
5	5	110
6	3	100
6	4	100
6	5	110
7	3	100
7	4	100
7	5	110
8	3	100
8	4	100
8	5	110
9	6	100
9	7	100
9	8	110
10	6	100
10	7	100
10	8	110
11	6	100
11	7	100
11	8	110
12	6	100
12	7	100
12	8	110
13	9	100
13	10	120
14	9	90
14	10	110
15	9	90
15	10	120
16	9	100
16	10	110
\.


--
-- TOC entry 3448 (class 0 OID 33566)
-- Dependencies: 217
-- Data for Name: types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.types (typeid, name) FROM stdin;
1	Автобус
2	Грузовик
3	КАМАЗ
4	Такси
\.


--
-- TOC entry 3487 (class 0 OID 0)
-- Dependencies: 236
-- Name: daily_reports_drid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.daily_reports_drid_seq', 2, true);


--
-- TOC entry 3488 (class 0 OID 0)
-- Dependencies: 229
-- Name: empl_emplid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.empl_emplid_seq', 40, true);


--
-- TOC entry 3489 (class 0 OID 0)
-- Dependencies: 218
-- Name: marshs_marshrutid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.marshs_marshrutid_seq', 10, true);


--
-- TOC entry 3490 (class 0 OID 0)
-- Dependencies: 222
-- Name: parts_partid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.parts_partid_seq', 7, true);


--
-- TOC entry 3491 (class 0 OID 0)
-- Dependencies: 224
-- Name: places_placeid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.places_placeid_seq', 9, true);


--
-- TOC entry 3492 (class 0 OID 0)
-- Dependencies: 220
-- Name: posts_postid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.posts_postid_seq', 11, true);


--
-- TOC entry 3493 (class 0 OID 0)
-- Dependencies: 232
-- Name: repair_reports_rrid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.repair_reports_rrid_seq', 4, true);


--
-- TOC entry 3494 (class 0 OID 0)
-- Dependencies: 226
-- Name: transport_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transport_id_seq', 16, true);


--
-- TOC entry 3495 (class 0 OID 0)
-- Dependencies: 238
-- Name: type_properties_propid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.type_properties_propid_seq', 10, true);


--
-- TOC entry 3496 (class 0 OID 0)
-- Dependencies: 216
-- Name: types_typeid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.types_typeid_seq', 4, true);


--
-- TOC entry 3288 (class 2606 OID 33720)
-- Name: daily_reports daily_reports_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_reports
    ADD CONSTRAINT daily_reports_pkey PRIMARY KEY (drid);


--
-- TOC entry 3284 (class 2606 OID 33649)
-- Name: empl empl_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empl
    ADD CONSTRAINT empl_pkey PRIMARY KEY (emplid);


--
-- TOC entry 3274 (class 2606 OID 33578)
-- Name: marshs marshs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.marshs
    ADD CONSTRAINT marshs_pkey PRIMARY KEY (marshrutid);


--
-- TOC entry 3278 (class 2606 OID 33592)
-- Name: parts parts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parts
    ADD CONSTRAINT parts_pkey PRIMARY KEY (partid);


--
-- TOC entry 3280 (class 2606 OID 33599)
-- Name: places places_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.places
    ADD CONSTRAINT places_pkey PRIMARY KEY (placeid);


--
-- TOC entry 3276 (class 2606 OID 33585)
-- Name: posts posts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.posts
    ADD CONSTRAINT posts_pkey PRIMARY KEY (postid);


--
-- TOC entry 3286 (class 2606 OID 33675)
-- Name: repair_reports repair_reports_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.repair_reports
    ADD CONSTRAINT repair_reports_pkey PRIMARY KEY (rrid);


--
-- TOC entry 3282 (class 2606 OID 33606)
-- Name: transport transport_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transport
    ADD CONSTRAINT transport_pkey PRIMARY KEY (id);


--
-- TOC entry 3290 (class 2606 OID 33732)
-- Name: type_properties type_properties_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.type_properties
    ADD CONSTRAINT type_properties_pkey PRIMARY KEY (propid);


--
-- TOC entry 3272 (class 2606 OID 33571)
-- Name: types types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.types
    ADD CONSTRAINT types_pkey PRIMARY KEY (typeid);


--
-- TOC entry 3303 (class 2606 OID 33721)
-- Name: daily_reports daily_reports_transportid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.daily_reports
    ADD CONSTRAINT daily_reports_transportid_fkey FOREIGN KEY (transportid) REFERENCES public.transport(id);


--
-- TOC entry 3294 (class 2606 OID 33650)
-- Name: empl empl_postid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.empl
    ADD CONSTRAINT empl_postid_fkey FOREIGN KEY (postid) REFERENCES public.posts(postid);


--
-- TOC entry 3299 (class 2606 OID 33695)
-- Name: repair_parts repair_parts_partid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.repair_parts
    ADD CONSTRAINT repair_parts_partid_fkey FOREIGN KEY (partid) REFERENCES public.parts(partid);


--
-- TOC entry 3300 (class 2606 OID 33690)
-- Name: repair_parts repair_parts_rrid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.repair_parts
    ADD CONSTRAINT repair_parts_rrid_fkey FOREIGN KEY (rrid) REFERENCES public.repair_reports(rrid);


--
-- TOC entry 3297 (class 2606 OID 33681)
-- Name: repair_reports repair_reports_emplid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.repair_reports
    ADD CONSTRAINT repair_reports_emplid_fkey FOREIGN KEY (emplid) REFERENCES public.empl(emplid);


--
-- TOC entry 3298 (class 2606 OID 33676)
-- Name: repair_reports repair_reports_transportid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.repair_reports
    ADD CONSTRAINT repair_reports_transportid_fkey FOREIGN KEY (transportid) REFERENCES public.transport(id);


--
-- TOC entry 3301 (class 2606 OID 33708)
-- Name: schedules schedules_marshrutid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_marshrutid_fkey FOREIGN KEY (marshrutid) REFERENCES public.marshs(marshrutid);


--
-- TOC entry 3302 (class 2606 OID 33703)
-- Name: schedules schedules_transportid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.schedules
    ADD CONSTRAINT schedules_transportid_fkey FOREIGN KEY (transportid) REFERENCES public.transport(id);


--
-- TOC entry 3295 (class 2606 OID 33663)
-- Name: transport_empl transport_empl_emplid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transport_empl
    ADD CONSTRAINT transport_empl_emplid_fkey FOREIGN KEY (emplid) REFERENCES public.empl(emplid);


--
-- TOC entry 3296 (class 2606 OID 33658)
-- Name: transport_empl transport_empl_transportid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transport_empl
    ADD CONSTRAINT transport_empl_transportid_fkey FOREIGN KEY (transportid) REFERENCES public.transport(id);


--
-- TOC entry 3291 (class 2606 OID 33612)
-- Name: transport transport_placeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transport
    ADD CONSTRAINT transport_placeid_fkey FOREIGN KEY (placeid) REFERENCES public.places(placeid);


--
-- TOC entry 3292 (class 2606 OID 33607)
-- Name: transport transport_typeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transport
    ADD CONSTRAINT transport_typeid_fkey FOREIGN KEY (typeid) REFERENCES public.types(typeid);


--
-- TOC entry 3304 (class 2606 OID 33733)
-- Name: type_properties type_properties_typeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.type_properties
    ADD CONSTRAINT type_properties_typeid_fkey FOREIGN KEY (typeid) REFERENCES public.types(typeid);


--
-- TOC entry 3293 (class 2606 OID 33632)
-- Name: type_value type_value_transportid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.type_value
    ADD CONSTRAINT type_value_transportid_fkey FOREIGN KEY (transportid) REFERENCES public.transport(id);


-- Completed on 2023-06-10 10:56:08

--
-- PostgreSQL database dump complete
--


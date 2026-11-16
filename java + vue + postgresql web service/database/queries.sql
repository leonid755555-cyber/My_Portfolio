--  1. Получить данные об автопарке предприятия.

SELECT *
FROM GetAutoList();

DROP FUNCTION GetAutoList;
CREATE OR REPLACE FUNCTION GetAutoList()
    RETURNS TABLE
    (
        transport_Id    INTEGER,
        type           VARCHAR,
        place          VARCHAR,
        dateOfPurchase DATE,
        workingLife    INTERVAL
    )
AS
$$
BEGIN
    RETURN QUERY
        SELECT tr.id,
               t.name,
               p.name,
               tr.dt,
               tr.working_life
        FROM transport AS tr
                 INNER JOIN types AS t ON tr.type_Id = t.Id
                 INNER JOIN places AS p ON p.Id = tr.place_Id;
END;
$$ LANGUAGE plpgsql;
--  2-3. Получить перечень и общее число водителей по предприятию, по указанной автомашине.

SELECT *
FROM GetDriverList('{}');

DROP FUNCTION GetDriverList;
CREATE OR REPLACE FUNCTION GetDriverList(
    auto_Id INTEGER[]
)
    RETURNS TABLE
    (
        empl_Id          INTEGER,
        name            VARCHAR,
        transport_Id     INTEGER,
        typeName        VARCHAR
    )
AS
$$
BEGIN
    RETURN QUERY
        SELECT e.Id,
               e.FIO,
               te.transport_Id,
               types.name
        FROM empl AS e
                 INNER JOIN transport_empl AS te ON e.Id = te.empl_Id AND e.post_Id = 1
                 INNER JOIN transport AS tr ON tr.Id = te.transport_Id
                 INNER JOIN types ON types.Id = tr.type_Id
        WHERE ((cardinality(auto_Id) != 0 AND te.transport_Id = ANY (auto_Id)) OR cardinality(auto_Id) = 0)
        order by te.transport_Id;
END;
$$ LANGUAGE plpgsql;
--  4. Получить данные о распределении пассажирского автотранспорта по маршрутам.

SELECT *
FROM GetSchedule();

DROP FUNCTION GetSchedule;
CREATE OR REPLACE FUNCTION GetSchedule()
    RETURNS TABLE
    (
        nameOfMarshrut VARCHAR,
        transport_Id  INTEGER
    )
AS
$$
BEGIN
    RETURN QUERY
        SELECT m.name,
               sh.transport_Id
        FROM schedules AS sh
                 INNER JOIN marshs AS m ON m.Id = sh.marshrut_Id;
END;
$$ LANGUAGE plpgsql;

--  5. Получить сведения о пробеге автотранспорта определенной категории или конкретной автомашины за указанный день, месяц, год.

CREATE OR REPLACE FUNCTION GetStartDate(anyarray)
    RETURNS anyelement
    LANGUAGE SQL
AS
$$
SELECT min(elements)
FROM unnest($1) elements
$$;

CREATE OR REPLACE FUNCTION GetEndDate(anyarray)
    RETURNS anyelement
    LANGUAGE SQL
AS
$$
SELECT max(elements)
FROM unnest($1) elements
$$;

SELECT *
FROM GetMileage('{}','{}','{}');

DROP FUNCTION GetMileage;
CREATE OR REPLACE FUNCTION GetMileage(
    auto_Id   INTEGER[],
    typeName VARCHAR[],
    dateList  DATE[]
)
    RETURNS TABLE
            (
                mileage  BIGINT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT sum(dr.mileage)
        FROM types 
                 INNER JOIN transport AS tr ON types.Id = tr.type_Id
                 INNER JOIN daily_reports AS dr ON dr.transport_Id = tr.Id
        WHERE (((cardinality(auto_Id) != 0 AND dr.transport_Id = ANY (auto_Id)) OR cardinality(auto_Id) = 0) AND
              ((cardinality(typeName) != 0 AND types.name = ANY (typeName)) OR cardinality(typeName) = 0)) AND
              ((cardinality(dateList) != 0 AND dr.report_date BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) OR 
              cardinality(dateList) = 0);
END;
$$ LANGUAGE plpgsql;

--  6. Получить данные о числе ремонтов и их стоимости для автотранспорта определенной категории, отдельной марки автотранспорта или указанной автомашины за указанный период.

SELECT *
FROM GetRepairCountAndCost('{}','{}','{}');

DROP FUNCTION GetRepairCountAndCost;
CREATE OR REPLACE FUNCTION GetRepairCountAndCost(
    auto_Id   INTEGER[],
    typeName VARCHAR[],
    dateList  DATE[]
)
    RETURNS TABLE
            (
                CountOfRepairs  BIGINT,
                CostOfRepairs   BIGINT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT 
            count(rr.id),
            sum(rr.cost)
        FROM types 
                 INNER JOIN transport AS tr ON types.Id = tr.type_Id
                 INNER JOIN repair_reports AS rr ON rr.transport_Id = tr.Id
        WHERE (((cardinality(auto_Id) != 0 AND rr.transport_Id = ANY (auto_Id)) OR cardinality(auto_Id) = 0) AND
              ((cardinality(typeName) != 0 AND types.name = ANY (typeName)) OR cardinality(typeName) = 0)) AND
              ((cardinality(dateList) != 0 AND (rr.date_of_receipt BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) AND 
                                               (rr.completion_date BETWEEN GetStartDate(dateList) AND GetEndDate(dateList))) OR 
              cardinality(dateList) = 0);
END;
$$ LANGUAGE plpgsql;

--  7. Получить данные о подчиненности персонала: рабочие - бригадиры - мастера - начальники участков и цехов.

SELECT *
FROM GetPostInfo();

DROP FUNCTION GetPostInfo;
CREATE OR REPLACE FUNCTION GetPostInfo()
    RETURNS TABLE
            (
                FIO         VARCHAR,
                post        VARCHAR
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT e.FIO,
               p.name
        FROM empl AS e
            INNER JOIN posts AS p ON p.Id = e.post_Id
        ORDER BY name;
END;
$$ LANGUAGE plpgsql;

--  8. Получить сведения о наличии гаражного хозяйства в целом и по каждой категории транспорта.
SELECT *
FROM GetPlaceInfo('{}');

DROP FUNCTION GetPlaceInfo;
CREATE OR REPLACE FUNCTION GetPlaceInfo(
    typeName VARCHAR[]
)
    RETURNS TABLE
            (
                place   VARCHAR,
                type    VARCHAR
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT distinct p.name,
               types.name
        FROM transport AS tr
                 INNER JOIN types ON tr.type_Id = types.Id
                 INNER JOIN places AS p ON p.Id = tr.place_Id
        WHERE ((cardinality(typeName) != 0 AND types.name = ANY (typeName)) OR cardinality(typeName) = 0)
        ORDER BY p.name;
END;
$$ LANGUAGE plpgsql;

--  9. Получить данные о распределении автотранспорта на предприятии.

SELECT *
FROM GetAutoPlaceInfo();

DROP FUNCTION GetAutoPlaceInfo;
CREATE OR REPLACE FUNCTION GetAutoPlaceInfo()
    RETURNS TABLE
            (
                transport_Id    INTEGER,
                place          VARCHAR
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT 
               tr.Id,
               p.name
        FROM transport AS tr
                 INNER JOIN places AS p ON p.Id = tr.place_Id
        ORDER BY tr.Id;
END;
$$ LANGUAGE plpgsql;

-- 10. Получить сведения о грузоперевозках, выполненных указанной автомашиной за обозначенный период.

SELECT *
FROM GetCargoTransportation('{}','{}','{}');

DROP FUNCTION GetCargoTransportation;
CREATE OR REPLACE FUNCTION GetCargoTransportation(
    auto_Id   INTEGER[],
    typeName VARCHAR[],
    dateList  DATE[]
)
    RETURNS TABLE
            (
                CargoTransportation  BIGINT
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT sum(dr.cargo_transportation)
        FROM types 
                 INNER JOIN transport AS tr ON types.Id = tr.type_Id
                 INNER JOIN daily_reports AS dr ON dr.transport_Id = tr.Id
        WHERE (((cardinality(auto_Id) != 0 AND dr.transport_Id = ANY (auto_Id)) OR cardinality(auto_Id) = 0) AND
              ((cardinality(typeName) != 0 AND types.name = ANY (typeName)) OR cardinality(typeName) = 0)) AND
              ((cardinality(dateList) != 0 AND dr.report_date BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) OR 
              cardinality(dateList) = 0);
END;
$$ LANGUAGE plpgsql;

-- 11. Получить данные о числе использованных для ремонта указанных узлов и агрегатов для транспорта определенной категории, отдельной марки автотранспорта или конкретной автомашины за указанный период.

SELECT *
FROM GetRepairPartsInfo('{}','{}','{Автобус}','{15-04-23,25-05-23}');

DROP FUNCTION GetRepairPartsInfo;
CREATE OR REPLACE FUNCTION GetRepairPartsInfo(
    partsList VARCHAR[],
    auto_Id    INTEGER[],
    tName  VARCHAR[],
    dateList  DATE[]
)
    RETURNS TABLE
            (
                partName  VARCHAR,
                transport_Id  INTEGER,
                typeName  VARCHAR,
                CountOfParts   INTEGER
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT 
            p.name,
            tr.Id,
            t.name,
            rp.count
        FROM types 
                 INNER JOIN transport AS tr ON types.Id = tr.type_Id
                 INNER JOIN repair_reports AS rr ON rr.transport_Id = tr.Id
                 INNER JOIN repair_parts AS rp ON rr.Id = rp.rrId
                 INNER JOIN parts AS p ON p.Id = rp.partId
                 INNER JOIN types AS t ON t.Id = tr.type_Id
        WHERE (((cardinality(auto_Id) != 0 AND rr.transport_Id = ANY (auto_Id)) OR cardinality(auto_Id) = 0) AND
              ((cardinality(tName) != 0 AND types.name = ANY (tName)) OR cardinality(tName) = 0)) AND
              ((cardinality(partsList) != 0 AND p.name = ANY (partsList)) OR cardinality(partsList) = 0) AND
              ((cardinality(dateList) != 0 AND (rr.date_of_receipt BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) AND 
                                               (rr.completion_date BETWEEN GetStartDate(dateList) AND GetEndDate(dateList))) OR 
              cardinality(dateList) = 0)
        ORDER BY p.name;
END;
$$ LANGUAGE plpgsql;
-- 12-1. Получить сведения о полученной автотехники за указанный период.

SELECT * 
FROM GetDecommissioned('{2015-07-25,2029-07-27}');

DROP FUNCTION GetDecommissionedAndPurchased;
CREATE OR REPLACE FUNCTION GetDecommissionedAndPurchased(
    dateList DATE[]
)
RETURNS TABLE(
    transport_Id    INTEGER,
    type           VARCHAR,
    place          VARCHAR,
    dateOfPurchase DATE,
    workingLife    INTERVAL,
    decommission   BOOL,
    purchased      BOOL     
)
AS
$$
BEGIN
    RETURN QUERY
    SELECT tr.id,
           t.name,
           p.name,
           tr.dt,
           tr.working_life,
    FROM transport AS tr
        INNER JOIN types AS t ON tr.type_Id = t.Id
        INNER JOIN places AS p ON p.Id = tr.place_Id
    WHERE (cardinality(dateList) != 0 AND (tr.dt BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) OR  cardinality(dateList) = 0 
    ORDER BY dateOfPurchase;
END;
$$ LANGUAGE plpgsql;

-- 12-2. Получить сведения о списанной автотехники за указанный период.

SELECT * 
FROM GetPurchased('{2015-07-25,2029-07-27}');

DROP FUNCTION GetDecommissionedAndPurchased;
CREATE OR REPLACE FUNCTION GetDecommissionedAndPurchased(
    dateList DATE[]
)
RETURNS TABLE(
    transport_Id    INTEGER,
    type           VARCHAR,
    place          VARCHAR,
    dateOfPurchase DATE,
    workingLife    INTERVAL,
    decommission   BOOL,
    purchased      BOOL     
)
AS
$$
BEGIN
    RETURN QUERY
    SELECT tr.id,
           t.name,
           p.name,
           tr.dt,
           tr.working_life,
    FROM transport AS tr
        INNER JOIN types AS t ON tr.type_Id = t.Id
        INNER JOIN places AS p ON p.Id = tr.place_Id
    WHERE (cardinality(dateList) != 0 AND ((tr.dt + tr.working_life) BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)))
      OR   cardinality(dateList) = 0 
    ORDER BY dateOfPurchase;
END;
$$ LANGUAGE plpgsql;


-- 13. Получить состав подчиненных указанного бригадира, мастера и пр.

SELECT * 
FROM GetSubordinates('{24}');

DROP FUNCTION GetSubordinates;
CREATE OR REPLACE FUNCTION GetSubordinates(
    personId INTEGER[]
)
    RETURNS TABLE(
                     Id   INTEGER,
                     FIO VARCHAR,
                     post VARCHAR
                 )
AS
$$
BEGIN
    RETURN QUERY
        SELECT 
               e.Id,
               e.FIO,
               p.name
        FROM empl AS e
                 INNER JOIN posts AS p ON e.post_Id = p.Id
        WHERE ((cardinality(personId) != 0 AND e.headId = ANY (personId)) OR cardinality(personId) = 0)
        ORDER BY p.name;
END;
$$ LANGUAGE plpgsql;
-- 14. Получить данные о работах, выполненных указанным специалистом (сварщиком, слесарем и т.д.) за обозначенный период в целом и по конкретной автомашине.

SELECT * 
FROM GetRepairInfo('{3}', '{2023-04-05,2023-05-15}', '{1}');

DROP FUNCTION GetRepairInfo;
CREATE OR REPLACE FUNCTION GetRepairInfo(
    personId INTEGER[],
    dateList DATE[],
    tId INTEGER[]
)
    RETURNS TABLE(
                     dateOfReceipt          DATE,
                     completionDate         DATE,
                     transportIdentifier    INTEGER,
                     workType               VARCHAR,
                     costOfRepair           INTEGER,
                     emplIdentifier         INTEGER,
                     name                   VARCHAR
                 )
AS
$$
BEGIN
    RETURN QUERY
        SELECT date_of_receipt,
               completion_date,
               transport_Id,
               work_type,
               cost,
               rr.empl_Id,
               FIO
        FROM repair_reports AS rr
            INNER JOIN empl AS e ON e.Id = rr.empl_Id
        WHERE ((cardinality(personId) != 0 AND rr.empl_Id = ANY (personId)) OR cardinality(personId) = 0)
          AND ((cardinality(tId) != 0 AND transport_Id = ANY (tId)) OR cardinality(tId) = 0)
          AND ((cardinality(dateList) != 0 AND (date_of_receipt BETWEEN GetStartDate(dateList) AND GetEndDate(dateList)) AND 
                                               (completion_date BETWEEN GetStartDate(dateList) AND GetEndDate(dateList))) OR 
              cardinality(dateList) = 0)
        ORDER BY date_of_receipt;
END;
$$ LANGUAGE plpgsql;
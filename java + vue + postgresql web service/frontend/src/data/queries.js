import AutoListQuery from "@/components/Queries/AutoListQuery";
import AutoPlaceInfoQuery from "@/components/Queries/AutoPlaceInfoQuery";
import CargoTransportationQuery from "@/components/Queries/CargoTransportationQuery";
import PurchasedQuery from "@/components/Queries/PurchasedQuery";
import DecommissionedQuery from "@/components/Queries/DecommissionedQuery";
import DriverListQuery from "@/components/Queries/DriverListQuery";
import MileageQuery from "@/components/Queries/MileageQuery";
import PlaceInfoQuery from "@/components/Queries/PlaceInfoQuery";
import PostInfoQuery from "@/components/Queries/PostInfoQuery";
import RepairCountAndCostQuery from "@/components/Queries/RepairCountAndCostQuery";
import RepairInfoQuery from "@/components/Queries/RepairInfoQuery";
import RepairPartsInfoQuery from "@/components/Queries/RepairPartsInfoQuery";
import ScheduleQuery from "@/components/Queries/ScheduleQuery";
import SubordinatesQuery from "@/components/Queries/SubordinatesQuery";
import SQLPage from "@/components/Queries/SQLPage";

const queries = [
    {
        id: 1,
        name: "Auto List",
        path: "/transport/list",
        component: AutoListQuery,
        props: true
    },
    {
        id: 2,
        name: "Auto Place Info",
        path: "/transport/place_info",
        component: AutoPlaceInfoQuery,
        props: true
    },
    {
        id: 3,
        name: "Cargo Transportation",
        path: "/daily_reports/cargo_transportation",
        component: CargoTransportationQuery,
        props: true
    },
    {
        id: 4,
        name: "Decommissioned",
        path: "/transport/decommissioned",
        component: DecommissionedQuery,
        props: true
    },
    {
        id: 5,
        name: "Purchased",
        path: "/transport/purchased",
        component: PurchasedQuery,
        props: true
    },
    {
        id: 6,
        name: "Driver List",
        path: "/empl/driver_list",
        component: DriverListQuery,
        props: true
    },
    {
        id: 7,
        name: "Mileage",
        path: "/daily_reports/mileage",
        component: MileageQuery,
        props: true
    },
    {
        id: 8,
        name: "Place Info",
        path: "/transport/info",
        component: PlaceInfoQuery,
        props: true
    },
    {
        id: 9,
        name: "Post Info",
        path: "/empl/post_info",
        component: PostInfoQuery,
        props: true
    },
    {
        id: 10,
        name: "Repair Info",
        path: "/repair_reports/info",
        component: RepairInfoQuery,
        props: true
    },
    {
        id: 11,
        name: "Repair Count And Cost",
        path: "/repair_reports/count_And_cost",
        component: RepairCountAndCostQuery,
        props: true
    },
    {
        id: 12,
        name: "Repair Parts Info",
        path: "/repair_reports/parts_info",
        component: RepairPartsInfoQuery,
        props: true
    },
    {
        id: 13,
        name: "Schedule",
        path: "/schedules/schedule",
        component: ScheduleQuery,
        props: true
    },
    {
        id: 14,
        name: "Subordinates",
        path: "/empl/subordinates",
        component: SubordinatesQuery,
        props: true
    },
    {
        id: 15,
        name: "SQL Page",
        path: '/transport/sql',
        component: SQLPage,
        props: true
    }
]

export default queries;
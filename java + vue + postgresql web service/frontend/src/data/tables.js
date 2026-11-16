import Daily_reportsTable from "@/components/Tables/Daily_reportsTable";
import EmplTable from "@/components/Tables/EmplTable";
import MarshsTable from "@/components/Tables/MarshsTable";
import PartsTable from "@/components/Tables/PartsTable";
import PlacesTable from "@/components/Tables/PlacesTable";
import PostsTable from "@/components/Tables/PostsTable";
import Repair_partsTable from "@/components/Tables/Repair_partsTable";
import Repair_reportsTable from "@/components/Tables/Repair_reportsTable";
import SchedulesTable from "@/components/Tables/SchedulesTable";
import Transport_emplTable from "@/components/Tables/Transport_emplTable";
import TransportTable from "@/components/Tables/TransportTable";
import Type_propertiesTable from "@/components/Tables/Type_propertiesTable";
import Type_valueTable from "@/components/Tables/Type_valueTable";
import TypesTable from "@/components/Tables/TypesTable";

const tables = [
    {
        id: 1,
        name: "Daily Reports Table",
        path: "/daily_reports",
        component: Daily_reportsTable,
        props: true
    },
    {
        id: 2,
        name: "Employe Table",
        path: "/empl",
        component: EmplTable,
        props: true
    },
    {
        id: 3,
        name: "Marshs Table",
        path: "/marshs",
        component: MarshsTable,
        props: true
    },
    {
        id: 4,
        name: "Parts Table",
        path: "/parts",
        component: PartsTable,
        props: true
    },
    {
        id: 5,
        name: "Places Table",
        path: "/places",
        component: PlacesTable,
        props: true
    },
    {
        id: 6,
        name: "Posts Table",
        path: "/posts",
        component: PostsTable,
        props: true
    },
    {
        id: 7,
        name: "Repair Parts Table",
        path: "/repair_parts",
        component: Repair_partsTable,
        props: true
    },
    {
        id: 8,
        name: "Repair Reports Table",
        path: "/repair_reports",
        component: Repair_reportsTable,
        props: true
    },
    {
        id: 9,
        name: "Schedules Table",
        path: "/schedules",
        component: SchedulesTable,
        props: true
    },
    {
        id: 10,
        name: "Transport Empl Table",
        path: "/transport_empl",
        component: Transport_emplTable,
        props: true
    },
    {
        id: 11,
        name: "Transport Table",
        path: "/transport",
        component: TransportTable,
        props: true
    },
    {
        id: 12,
        name: "Type Properties Table",
        path: "/type_properties",
        component: Type_propertiesTable,
        props: true
    },
    {
        id: 13,
        name: "Type Value Table",
        path: "/type_value",
        component: Type_valueTable,
        props: true
    },
    {
        id: 14,
        name: "Types Table",
        path: "/types",
        component: TypesTable,
        props: true
    }
]

export default tables;
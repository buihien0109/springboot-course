import { useContext } from "react";
import { Navigate, Outlet } from "react-router-dom";
import Context from "../app/context/Context";

function PrivateRoutes() {
    const { auth } = useContext(Context);

    if(!auth || !auth.is_authenticated) {
        return <Navigate to={"/login"}/>
    }

    return (
        <>
            <Outlet />
        </>
    );
}

export default PrivateRoutes;
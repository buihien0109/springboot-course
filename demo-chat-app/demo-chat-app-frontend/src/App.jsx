import { Route, Routes } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Chat from "./page/chat/Chat";
import Login from "./page/login/Login";
import Register from "./page/register/Register";
import PrivateRoutes from "./components/PrivateRoutes";

function App() {
    return (
        <>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                <Route element={<PrivateRoutes />}>
                    <Route path="/chat" element={<Chat />} />
                </Route>
            </Routes>
            <ToastContainer autoClose={2000} />
        </>
    );
}

export default App;

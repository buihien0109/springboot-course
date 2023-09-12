import axios from "axios";
import React, { useContext, useState } from "react";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import Context from "../../app/context/Context";
import { addAuth } from "../../app/store/auth/actions";

const API_URL = "http://localhost:8080/api/login";
function Login() {
    const navigate = useNavigate();
    const { auth, dispatchAuth } = useContext(Context);
    const [name, setName] = useState("");
    const [secret, setSecret] = useState("");

    // Nếu đã login -> chuyển hướng sang trang chat
    if (auth && auth.is_authenticated) {
        return <Navigate to={"/chat"} />;
    }

    const handleLogin = (e) => {
        e.preventDefault();

        axios
            .post(API_URL, {
                name,
                secret,
            })
            .then((res) => {
                if (res.status === 200) {
                    toast.success("Đăng nhập thành công");
                    dispatchAuth(addAuth({ ...res.data, secret }));
                    setTimeout(() => {
                        navigate("/chat");
                    }, 1500);
                }
            })
            .catch((err) => console.log(err));
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-lg-4">
                    <form
                        className="p-5 rounded bg-light"
                        onSubmit={handleLogin}
                    >
                        <h2 className="fs-2 mb-5 text-center">Login</h2>
                        <div className="mb-3">
                            <label
                                htmlFor="name"
                                className="form-label fw-bold"
                            >
                                Name
                            </label>
                            <input
                                type="text"
                                className="form-control"
                                id="name"
                                autoComplete="off"
                                required
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />
                        </div>
                        <div className="mb-3">
                            <label
                                htmlFor="secret"
                                className="form-label fw-bold"
                            >
                                Secret
                            </label>
                            <input
                                type="password"
                                className="form-control"
                                id="secret"
                                autoComplete="off"
                                required
                                value={secret}
                                onChange={(e) => setSecret(e.target.value)}
                            />
                        </div>
                        <div className="mb-3 d-flex justify-content-center">
                            <button
                                type="submit"
                                className="btn btn-primary d-block w-100"
                            >
                                Login
                            </button>
                        </div>
                        <div>
                            <p className="text-muted">
                                Nếu chưa có tài khoản{" "}
                                <Link to={"/register"}>Bấm đăng ký</Link>
                            </p>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Login;

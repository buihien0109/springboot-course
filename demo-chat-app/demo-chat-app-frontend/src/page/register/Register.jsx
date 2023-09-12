import axios from "axios";
import React, { useContext, useState } from "react";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import Context from "../../app/context/Context";

const API_URL = "http://localhost:8080/api/register";
function Register() {
    const { auth } = useContext(Context);
    const navigate = useNavigate();
    const [name, setName] = useState("");
    const [secret, setSecret] = useState("");
    const [email, setEmail] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");

    // Nếu đã login -> chuyển hướng sang trang chat
    if (auth && auth.is_authenticated) {
        return <Navigate to={"/chat"} />;
    }

    const handleRegister = (e) => {
        e.preventDefault();

        axios
            .post(API_URL, {
                name,
                secret,
                email,
                firstName,
                lastName,
            })
            .then((res) => {
                if (res.status === 200) {
                    console.log(res);
                    toast.success("Đăng ký thành công");
                    setTimeout(() => {
                        navigate("/login");
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
                        className="p-4 rounded bg-light"
                        onSubmit={handleRegister}
                    >
                        <h2 className="fs-2 mb-5 text-center">Register</h2>
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
                        <div className="mb-3">
                            <label
                                htmlFor="email"
                                className="form-label fw-bold"
                            >
                                Email
                            </label>
                            <input
                                type="email"
                                className="form-control"
                                id="email"
                                autoComplete="off"
                                required
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>
                        <div className="mb-3">
                            <label
                                htmlFor="first-name"
                                className="form-label fw-bold"
                            >
                                Firstname
                            </label>
                            <input
                                type="text"
                                className="form-control"
                                id="first-name"
                                autoComplete="off"
                                required
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                            />
                        </div>
                        <div className="mb-3">
                            <label
                                htmlFor="last-name"
                                className="form-label fw-bold"
                            >
                                Lastname
                            </label>
                            <input
                                type="text"
                                className="form-control"
                                id="last-name"
                                autoComplete="off"
                                required
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                            />
                        </div>
                        <div className="mb-3 d-flex justify-content-center">
                            <button
                                type="submit"
                                className="btn btn-primary d-block w-100"
                            >
                                Register
                            </button>
                        </div>
                        <div>
                            <p className="text-muted">
                                Quay lại <Link to={"/login"}>Đăng nhập</Link>
                            </p>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default Register;

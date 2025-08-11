import Sidebar from "./Sidebar";
import { Outlet } from "react-router-dom";
import logo from "../assets/logo1trimmed.png";
import "../styles/MainLayout.css";
import { useUserContext } from "../context/UserContext";

export default function MainLayout() {
  const { user } = useUserContext();
  return (
    <div className="layout-wrapper">
      <header className="main-header">
        {user && (user.profileImage || user.picture) && (
          <img
            src={user.profileImage || user.picture}
            alt="Profile"
            className="user-profile-image"
          />
        )}
        <img src={logo} alt="EZDrive Logo" className="logo" />
      </header>

      <div className="app-container">
        <Sidebar />
        <main className="main-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
}

import Sidebar from "./Sidebar";
import { Outlet } from "react-router-dom";
import logo from "../assets/logo1.png";
import "../styles/MainLayout.css";

export default function MainLayout() {
  return (
    <div className="layout-wrapper">
      <header className="main-header">
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

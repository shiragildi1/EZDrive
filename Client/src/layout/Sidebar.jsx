import HomeIcon from "@mui/icons-material/Home";
import ListIcon from "@mui/icons-material/List";
import PersonIcon from "@mui/icons-material/Person";
import ChatIcon from "@mui/icons-material/Chat";
import MenuIcon from "@mui/icons-material/Menu";
import { useState } from "react";
import { NavLink } from "react-router-dom";
import "../styles/Sidebar.css";

export default function Sidebar() {
  const [open, setOpen] = useState(false);

  return (
    <div className="layout-container">
      <aside className={`sidebar-fixed ${open ? "open" : ""}`}>
        <button className="menu-button" onClick={() => setOpen(!open)}>
          <MenuIcon />
        </button>
        <NavLink to="/HomePage">
          <HomeIcon /> {open && <span>עמוד בית</span>}
        </NavLink>
        <NavLink to="/rankingPage">
          <ListIcon /> {open && <span>דירוג משתמשים</span>}
        </NavLink>
        <NavLink to="/profilePage">
          <PersonIcon /> {open && <span>הפרופיל שלי</span>}
        </NavLink>
        <NavLink to="/chatPage">
          <ChatIcon /> {open && <span>צ'אט</span>}
        </NavLink>
      </aside>
    </div>
  );
}

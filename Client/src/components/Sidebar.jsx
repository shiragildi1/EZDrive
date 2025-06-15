import HomeIcon from "@mui/icons-material/Home";
import ListIcon from "@mui/icons-material/List";
import PersonIcon from "@mui/icons-material/Person";
import { NavLink } from "react-router-dom";
import "../styles/Sidebar.css";
import logo from '../assets/logo.png';



export default function Sidebar() {
  return (
    <header className="header">  
      <div className="bar-rtl"> 
        <aside className="sidebar">
          <img src={logo} alt="" className="logo" />
          <ul>
            <div className="links">
              <li>
                <NavLink to="/HomePage" >
                  <HomeIcon /> עמוד בית 
                </NavLink>
              </li>
              <li>
                <NavLink to="/rankingPage">
                    <ListIcon /> דירוג משתמשים
                </NavLink>
              </li>
              <li>
                <NavLink to="/profilePage">
                    <PersonIcon /> הפרופיל שלי
                </NavLink>
              </li>
            </div>
          </ul>
        </aside>
      </div>
    </header>
  );
}

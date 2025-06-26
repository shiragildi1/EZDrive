import "../styles/Home.css";
import TrafficIcon from "@mui/icons-material/Traffic";
import StopIcon from "@mui/icons-material/Stop";
import SafetyCheckIcon from "@mui/icons-material/SafetyCheck";
import BuildIcon from "@mui/icons-material/Build";
import backgroundImage from "../assets/home.png";

export default function HomePage() {
  return (
    <div className="home-background">
      <img src={backgroundImage} alt="רקע כביש" className="bg-image" />
      <div className="overlay-content">
        <div className="slogan1">?רוצה ללמוד תיאוריה</div>
        <div className="slogan2">.שחק אותה</div>
        <div className="squares">
          <div className="square">
            <TrafficIcon className="icon" />
            <h3>חוקי תנועה</h3>
          </div>
          <div className="square">
            <StopIcon className="icon" />
            <h3>תמרורים</h3>
          </div>
          <div className="square">
            <SafetyCheckIcon className="icon" />
            <h3>בטיחות</h3>
          </div>
          <div className="square">
            <BuildIcon className="icon" />
            <h3>הכרת הרכב</h3>
          </div>
        </div>
      </div>
    </div>
  );
}

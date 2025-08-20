import { useEffect, useState } from "react";
import "../styles/profile.css";
import Box from "@mui/material/Box";
import CircularProgress from "@mui/material/CircularProgress";
import Typography from "@mui/material/Typography";
import { useUserContext } from "../context/UserContext";
import { getProfileStats } from "../services/ProfileService";


export default function ProfilePage() {
  const { user } = useUserContext();
  const [range, setRange] = useState("all");
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!user?.email) return;
    setLoading(true);
    getProfileStats(user.email, range)
      .then((data) => {
        setStats(data);
      })
      .catch((error) => {
        console.error("Error fetching profile stats:", error);
        setStats(null);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [user.email, range]);

  let trivia = { count: 0, average: 0 };
  let memory = { count: 0, average: 0 };
  let simulation = { count: 0, average: 0 };

  if (stats && stats.trivia) {
    trivia = stats.trivia;
  }
  if (stats && stats.memory) {
    memory = stats.memory;
  }
  if (stats && stats.simulation) {
    simulation = stats.simulation;
  }

  return (
    <div className="profile-page">
      <h1 className="profile-page-title">הפרופיל שלי</h1>
      <h2 className="profile-page-subtitle">הישגים</h2>

      <div className="filter-buttons">
        <button className="share-result" onClick={() => setRange("All")}>הכל</button>
        <button className="last-month" onClick={() => setRange("30")}>30 ימים אחרונים</button>
        <button className="last-week" onClick={() => setRange("7")}>7 ימים אחרונים</button>
        {/* <button className="share-result" onClick={() => {}}>שתף תוצאות</button> */}

      </div>
      
        <div className="profile-achievements-row">
          {/* זיכרון */}
          <AchievementCard
            title="זיכרון"
            percentage={memory.average}
            gamesCount={memory.count}
          />
          {/* טריוויה */}
          <AchievementCard
            title="טריוויה"
            percentage={trivia.average}
            gamesCount={trivia.count}
          />
          {/* סימולטור */}
          <AchievementCard
            title="סימולטור"
            percentage={simulation.average}
            gamesCount={simulation.count}
          />
        </div>
    </div>
  );
}

function AchievementCard({ title, percentage, gamesCount }) {
  return (
    <div className="achievement-card">
      <Box position="relative" display="inline-flex" className="circle-box">
        <CircularProgress
          variant="determinate"
          value={percentage}
          size={90}
          thickness={5}
          className="circle-progress"
        />
        <Box
          top={0}
          left={0}
          bottom={0}
          right={0}
          position="absolute"
          display="flex"
          alignItems="center"
          justifyContent="center"
          className="circle-center"
        >
          <Typography variant="h5" component="div" className="circle-text">
            {percentage}%
          </Typography>
        </Box>
      </Box>
      <div className="achievement-title">{title}</div>
      <div className="achievement-label">{gamesCount} משחקים</div>
    </div>
  );
}

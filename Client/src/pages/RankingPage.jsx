import "../styles/Ranking.css";
import { useUserContext } from "../context/UserContext";
import { getRankingeStats } from "../services/RankingService";
import { useEffect, useState } from "react";

export default function RankingPage() {
  const { user } = useUserContext();
  const [rankingData, setRankingData] = useState([]);
  const [userRank, setUserRank] = useState(null);

  useEffect(() => {
    getRankingeStats()
      .then((data) => {
        setRankingData(data);
        if (user?.email) {
          const idx = data.findIndex((row) => row.userEmail === user.email);
          setUserRank(idx !== -1 ? idx + 1 : null);
        }
      })
      .catch((error) => {
        console.error("Error fetching ranking stats:", error);
        setRankingData([]);
      });
  }, [user?.email]);
  return (
    <div className="ranking-page">
      <div className="ranking-header">
        <h1>
          הדירוג שלך: <span className="user-rank">{userRank}</span>
        </h1>
        <h1>!כל הכבוד</h1>
      </div>
      <table className="ranking-table">
        <thead>
          <tr>
            <th>ניקוד</th>
            <th>שם משתמש</th>
            <th>פרופיל</th>
            <th>דירוג</th>
          </tr>
        </thead>
        <tbody>
          {rankingData.map((row, idx) => (
            <tr
              key={row.userEmail}
              className={user?.email === row.userEmail ? "user-row" : ""}
            >
              <td>{row.totalScore}</td>
              <td>{row.userEmail}</td>
              <td>
                {row.profileImage && (
                  <img
                    src={row.profileImage}
                    alt="profile"
                    className="ranking-profile-img"
                  />
                )}
              </td>
              <td
                className={
                  user?.email === row.userEmail ? "user-rank-cell" : ""
                }
              >
                {idx + 1}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

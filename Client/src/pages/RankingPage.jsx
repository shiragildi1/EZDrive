import "../styles/Ranking.css";
import { useUserContext } from "../context/UserContext";
import { getRankingeStats } from "../services/RankingService";
import { useEffect, useState } from "react";

export default function RankingPage() {
  const { user } = useUserContext();
  const [rankingData, setRankingData] = useState([]);

  useEffect(() => {
    getRankingeStats()
      .then((data) => {
        setRankingData(data);
        console.log("Ranking data:", data);
      })
      .catch((error) => {
        console.error("Error fetching ranking stats:", error);
        setRankingData([]);
      });
  }, []);

  //   useEffect(() => {
  //     if (!user?.email) return;
  //     setLoading(true);
  //     getProfileStats(user.email, range)
  //       .then((data) => {
  //         setStats(data);
  //       })
  //       .catch((error) => {
  //         console.error("Error fetching profile stats:", error);
  //         setStats(null);
  //       })
  //       .finally(() => {
  //         setLoading(false);
  //       });
  //   }, [user?.email, range]);
  return (
    <div className="ranking-page">
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
            <tr key={row.userEmail}>
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
              <td>{idx + 1}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

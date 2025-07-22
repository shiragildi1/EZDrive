import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useEffect } from "react";
import { useUserContext } from "./context/UserContext";
import { getCurrentUser } from "./services/userService"; // פונקציה שמביאה את היוזר מהסשן

import SignUpPage from "./pages/SignUpPage";
import SignInPage from "./pages/SignInPage";
import OtpPage from "./pages/OtpPage";
import HomePage from "./pages/HomePage";
import RankingPage from "./pages/RankingPage";
import ProfilePage from "./pages/ProfilePage";
import ChatPage from "./pages/ChatPage";
import GamesPage from "./components/Games";
import TriviaGame from "./components/TriviaGame";
import MemoryGame from "./components/MemoryGame";
import MainLayout from "./layout/MainLayout";

function App() {
  const { setUser } = useUserContext();

  useEffect(() => {
    getCurrentUser()
      .then((user) => {
        setUser(user);
        console.log("User loaded from session:", user);
      })
      .catch(() => {
        setUser(null);
        console.log("No user found in session.");
      });
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        {/* עמודים ללא Sidebar */}
        <Route path="/" element={<SignUpPage />} />
        <Route path="/SignInPage" element={<SignInPage />} />
        <Route path="/OtpPage" element={<OtpPage />} />

        {/* עמודים עם Sidebar קבוע */}
        <Route path="/" element={<MainLayout />}>
          <Route path="HomePage" element={<HomePage />} />
          <Route path="RankingPage" element={<RankingPage />} />
          <Route path="ProfilePage" element={<ProfilePage />} />
          <Route path="ChatPage" element={<ChatPage />} />
          <Route path="Games" element={<GamesPage />} />
          <Route path="TriviaGame" element={<TriviaGame />} />
          <Route path="MemoryGame" element={<MemoryGame />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;

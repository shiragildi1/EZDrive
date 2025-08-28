import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useEffect } from "react";
import { useUserContext } from "./context/UserContext";

import SignUpPage from "./pages/SignUpPage";
import SignInPage from "./pages/SignInPage";
import OtpPage from "./pages/OtpPage";
import HomePage from "./pages/HomePage";
import RankingPage from "./pages/RankingPage";
import ProfilePage from "./pages/ProfilePage";
import ChatPage from "./pages/ChatPage";
import GamesPage from "./components/Games";
import TriviaGame from "./components/TriviaGame";
import EndOfTriviaPage from "./pages/EndOfTriviaPage";
import MemoryGame from "./components/MemoryGame";
import MainLayout from "./layout/MainLayout";
import EndOfMemoryPage from "./pages/EndOfMemoryPage";

function App() {
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
          <Route path="EndOfTriviaPage" element={<EndOfTriviaPage />} />
          <Route path="MemoryGame" element={<MemoryGame />} />
          <Route path="ProfilePage" element={<ProfilePage />} />
          <Route path="EndOfMemoryPage" element={<EndOfMemoryPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;

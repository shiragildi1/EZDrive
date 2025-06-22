import { BrowserRouter, Routes, Route } from "react-router-dom";
import SignUpPage from "./pages/SignUpPage";
import SignInPage from "./pages/SignInPage";
import OtpPage from "./pages/OtpPage";
import HomePage from "./pages/HomePage";
import RankingPage from "./pages/RankingPage";
import ProfilePage from "./pages/ProfilePage";
import ChatPage from "./pages/ChatPage";
import MainLayout from "./layout/MainLayout"; 

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SignUpPage />} />
        <Route path="/SignInPage" element={<SignInPage />} />
        <Route path="/OtpPage" element={<OtpPage />} />

        <Route path="/" element={<MainLayout />}>
          <Route path="HomePage" element={<HomePage />} />
          <Route path="RankingPage" element={<RankingPage />} />
          <Route path="ProfilePage" element={<ProfilePage />} />
          <Route path="ChatPage" element={<ChatPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;

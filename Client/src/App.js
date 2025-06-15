import { BrowserRouter, Routes, Route } from "react-router-dom";
import SignUpPage from "./pages/SignUpPage";
import SignInPage from "./pages/SignInPage";
import OtpPage from "./pages/OtpPage";
import Sidebar from "./components/Sidebar";
import HomePage from "./pages/HomePage";
import RankingPage from "./pages/RankingPage";
import ProfilePage from "./pages/ProfilePage";



function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SignUpPage />} />
        <Route path="/SignInPage" element={<SignInPage />} />
        <Route path="/OtpPage" element={<OtpPage />} />
        <Route
          path="/HomePage"
          element= {
            <div className="layout">
              <Sidebar />
              <HomePage />
            </div>
          }
        />
        <Route
          path="/RankingPage"
          element= {
            <div className="layout">
              <Sidebar />
              <RankingPage />
            </div>
          }
        />
        <Route
          path="/ProfilePage"
          element= {
            <div className="layout">
              <Sidebar />
              <ProfilePage />
            </div>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}


export default App;

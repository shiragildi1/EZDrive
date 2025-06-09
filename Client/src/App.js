import { BrowserRouter, Routes, Route } from "react-router-dom";
import SignUpPage from "./pages/SignUpPage";
import SignInPage from "./pages/SignInPage";
import OtpPage from "./pages/OtpPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<SignUpPage />} />
        <Route path="/SignInPage" element={<SignInPage />} />
        <Route path="/OtpPage" element={<OtpPage />} />
      </Routes>
    </BrowserRouter>
  );
}


export default App;

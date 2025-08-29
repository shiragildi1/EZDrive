import { Navigate } from "react-router-dom";
import { useUserContext } from "../context/UserContext";

export default function ProtectedRoute({ children }) {
  const { user } = useUserContext();
  if (!user) return <Navigate to="/SignInPage" replace />;
  return children;
}

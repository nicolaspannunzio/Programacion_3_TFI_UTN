import type { IUser } from "../types/IUser";
import { navigate } from "./navigate";

export function getSession(): IUser | null {
  const data = localStorage.getItem("userData");
  return data ? JSON.parse(data) : null;
}

export function saveSession(user: IUser): void {
  localStorage.setItem("userData", JSON.stringify(user));
}

export function clearSession(): void {
  localStorage.removeItem("userData");
}

export function logout(): void {
  clearSession();
  navigate("/src/pages/auth/login/login.html");
}

export function checkAuthUser(
  redirectIfNoSession: string,
  redirectIfWrongRole: string,
  rolRequerido: "ADMIN" | "USUARIO"
): void {
  const user = getSession();

  if (!user) {
    navigate(redirectIfNoSession);
    return;
  }

  if (user.rol !== rolRequerido) {
    navigate(redirectIfWrongRole);
  }
}
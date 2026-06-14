import { loginApi } from "../../../utils/api";
import { navigate } from "../../../utils/navigate";

const form = document.getElementById("login-form") as HTMLFormElement;
const inputEmail = document.getElementById("email") as HTMLInputElement;
const inputPassword = document.getElementById("password") as HTMLInputElement;
const errorMsg = document.getElementById("error-msg") as HTMLParagraphElement;

form.addEventListener("submit", async (e: SubmitEvent) => {
  e.preventDefault();

  const mail = inputEmail.value.trim();
  const contraseña = inputPassword.value.trim();

  try {
    const usuario = await loginApi(mail, contraseña);

    // Guardamos el usuario en localStorage para mantener la sesión
    localStorage.setItem("userData", JSON.stringify(usuario));

    if (usuario.rol === "ADMIN") {
      navigate("/src/pages/admin/home/home.html");
    } else {
      navigate("/src/pages/store/home/home.html");
    }
  } catch (error: any) {
    errorMsg.textContent = error.message || "Credenciales incorrectas";
    errorMsg.style.display = "block";
  }
});
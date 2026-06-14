import { registerApi } from "../../../utils/api";
import { navigate } from "../../../utils/navigate";

const form = document.getElementById("registro-form") as HTMLFormElement;
const inputNombre = document.getElementById("nombre") as HTMLInputElement;
const inputApellido = document.getElementById("apellido") as HTMLInputElement;
const inputEmail = document.getElementById("email") as HTMLInputElement;
const inputCelular = document.getElementById("celular") as HTMLInputElement;
const inputPassword = document.getElementById("password") as HTMLInputElement;
const errorMsg = document.getElementById("error-msg") as HTMLParagraphElement;

form.addEventListener("submit", async (e: SubmitEvent) => {
  e.preventDefault();

  const nombre = inputNombre.value.trim();
  const apellido = inputApellido.value.trim();
  const mail = inputEmail.value.trim();
  const celular = inputCelular.value.trim();
  const contraseña = inputPassword.value.trim();

  if (contraseña.length < 6) {
    errorMsg.textContent = "La contraseña debe tener al menos 6 caracteres.";
    errorMsg.style.display = "block";
    return;
  }

  try {
    await registerApi({ nombre, apellido, mail, celular, contraseña });
    navigate("/src/pages/auth/login/login.html");
  } catch (error: any) {
    errorMsg.textContent = error.message || "Error al registrarse.";
    errorMsg.style.display = "block";
  }
});
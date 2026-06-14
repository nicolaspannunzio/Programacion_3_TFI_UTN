import {
  getCategoriasApi,
  crearCategoriaApi,
  actualizarCategoriaApi,
  eliminarCategoriaApi,
} from "../../../utils/api";
import { getSession, logout } from "../../../utils/auth";

const session = getSession();
if (!session || session.rol !== "ADMIN") {
  window.location.replace("/src/pages/auth/login/login.html");
}

let editandoId: number | null = null;

function showModal(titulo: string, nombre = "", descripcion = ""): void {
  const modal = document.getElementById("modal");
  const modalTitle = document.getElementById("modal-title");
  const inputNombre = document.getElementById("nombre") as HTMLInputElement;
  const inputDesc = document.getElementById("descripcion") as HTMLTextAreaElement;
  const errorEl = document.getElementById("form-error");

  if (modalTitle) modalTitle.textContent = titulo;
  if (inputNombre) inputNombre.value = nombre;
  if (inputDesc) inputDesc.value = descripcion;
  if (errorEl) errorEl.style.display = "none";
  if (modal) modal.style.display = "flex";
}

function hideModal(): void {
  const modal = document.getElementById("modal");
  if (modal) modal.style.display = "none";
  editandoId = null;
}

async function cargarCategorias(): Promise<void> {
  const tbody = document.getElementById("categorias-tbody");
  const emptyMsg = document.getElementById("empty-msg");
  if (!tbody) return;

  tbody.innerHTML = "";

  const categorias = await getCategoriasApi();

  if (categorias.length === 0) {
    if (emptyMsg) emptyMsg.style.display = "block";
    return;
  }

  if (emptyMsg) emptyMsg.style.display = "none";

  categorias.forEach((cat: any) => {
    const tr = document.createElement("tr");

    const tdId = document.createElement("td");
    tdId.textContent = String(cat.id);

    const tdNombre = document.createElement("td");
    tdNombre.textContent = cat.nombre;

    const tdDesc = document.createElement("td");
    tdDesc.textContent = cat.descripcion ?? "";

    const tdAcciones = document.createElement("td");
    tdAcciones.className = "acciones";

    const btnEditar = document.createElement("button");
    btnEditar.className = "btn-secondary";
    btnEditar.textContent = "Editar";
    btnEditar.addEventListener("click", () => {
      editandoId = cat.id;
      showModal("Editar Categoría", cat.nombre, cat.descripcion ?? "");
    });

    const btnEliminar = document.createElement("button");
    btnEliminar.className = "btn-danger";
    btnEliminar.textContent = "Eliminar";
    btnEliminar.addEventListener("click", async () => {
      if (confirm(`¿Eliminar la categoría "${cat.nombre}"?`)) {
        await eliminarCategoriaApi(cat.id);
        await cargarCategorias();
      }
    });

    tdAcciones.append(btnEditar, btnEliminar);
    tr.append(tdId, tdNombre, tdDesc, tdAcciones);
    tbody.appendChild(tr);
  });
}

async function init(): Promise<void> {
  if (!session) return;

  await cargarCategorias();

  document.getElementById("btn-nueva")?.addEventListener("click", () => {
    editandoId = null;
    showModal("Nueva Categoría");
  });

  document.getElementById("btn-cancelar")?.addEventListener("click", () => {
    hideModal();
  });

  document.getElementById("categoria-form")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const nombre = (document.getElementById("nombre") as HTMLInputElement).value.trim();
    const descripcion = (document.getElementById("descripcion") as HTMLTextAreaElement).value.trim();
    const errorEl = document.getElementById("form-error");

    try {
      if (editandoId !== null) {
        await actualizarCategoriaApi(editandoId, { nombre, descripcion });
      } else {
        await crearCategoriaApi({ nombre, descripcion });
      }
      hideModal();
      await cargarCategorias();
    } catch (error: any) {
      if (errorEl) {
        errorEl.textContent = error.message || "Error al guardar.";
        errorEl.style.display = "block";
      }
    }
  });

  document.getElementById("logout-btn")?.addEventListener("click", () => logout());
}

init();
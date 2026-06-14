import {
  getProductosApi,
  getCategoriasApi,
  crearProductoApi,
  actualizarProductoApi,
  eliminarProductoApi,
} from "../../../utils/api";
import { getSession, logout } from "../../../utils/auth";

const session = getSession();
if (!session || session.rol !== "ADMIN") {
  window.location.replace("/src/pages/auth/login/login.html");
}

let editandoId: number | null = null;

async function cargarCategorias(): Promise<void> {
  const select = document.getElementById("categoria") as HTMLSelectElement;
  if (!select) return;

  const categorias = await getCategoriasApi();
  select.innerHTML = "";

  categorias.forEach((cat: any) => {
    const option = document.createElement("option");
    option.value = String(cat.id);
    option.textContent = cat.nombre;
    select.appendChild(option);
  });
}

function showModal(titulo: string, producto?: any): void {
  const modal = document.getElementById("modal");
  const modalTitle = document.getElementById("modal-title");
  const errorEl = document.getElementById("form-error");

  if (modalTitle) modalTitle.textContent = titulo;
  if (errorEl) errorEl.style.display = "none";

  const nombre = document.getElementById("nombre") as HTMLInputElement;
  const descripcion = document.getElementById("descripcion") as HTMLTextAreaElement;
  const precio = document.getElementById("precio") as HTMLInputElement;
  const stock = document.getElementById("stock") as HTMLInputElement;
  const imagen = document.getElementById("imagen") as HTMLInputElement;
  const disponible = document.getElementById("disponible") as HTMLInputElement;
  const categoria = document.getElementById("categoria") as HTMLSelectElement;

  if (producto) {
    nombre.value = producto.nombre ?? "";
    descripcion.value = producto.descripcion ?? "";
    precio.value = String(producto.precio ?? "");
    stock.value = String(producto.stock ?? "");
    imagen.value = producto.imagen ?? "";
    disponible.checked = producto.disponible ?? true;
    if (producto.categoria?.id) categoria.value = String(producto.categoria.id);
  } else {
    nombre.value = "";
    descripcion.value = "";
    precio.value = "";
    stock.value = "";
    imagen.value = "";
    disponible.checked = true;
  }

  if (modal) modal.style.display = "flex";
}

function hideModal(): void {
  const modal = document.getElementById("modal");
  if (modal) modal.style.display = "none";
  editandoId = null;
}

async function cargarProductos(): Promise<void> {
  const tbody = document.getElementById("productos-tbody");
  const emptyMsg = document.getElementById("empty-msg");
  if (!tbody) return;

  tbody.innerHTML = "";
  const productos = await getProductosApi();

  if (productos.length === 0) {
    if (emptyMsg) emptyMsg.style.display = "block";
    return;
  }

  if (emptyMsg) emptyMsg.style.display = "none";

  productos.forEach((prod: any) => {
    const tr = document.createElement("tr");

    const tdId = document.createElement("td");
    tdId.textContent = String(prod.id);

    const tdNombre = document.createElement("td");
    tdNombre.textContent = prod.nombre;

    const tdPrecio = document.createElement("td");
    tdPrecio.textContent = `$${prod.precio?.toLocaleString("es-AR")}`;

    const tdCategoria = document.createElement("td");
    tdCategoria.textContent = prod.categoria?.nombre ?? "-";

    const tdStock = document.createElement("td");
    tdStock.textContent = String(prod.stock);

    const tdEstado = document.createElement("td");
    tdEstado.textContent = prod.disponible ? "✅ Disponible" : "❌ No disponible";

    const tdAcciones = document.createElement("td");
    tdAcciones.className = "acciones";

    const btnEditar = document.createElement("button");
    btnEditar.className = "btn-secondary";
    btnEditar.textContent = "Editar";
    btnEditar.addEventListener("click", () => {
      editandoId = prod.id;
      showModal("Editar Producto", prod);
    });

    const btnEliminar = document.createElement("button");
    btnEliminar.className = "btn-danger";
    btnEliminar.textContent = "Eliminar";
    btnEliminar.addEventListener("click", async () => {
      if (confirm(`¿Eliminar el producto "${prod.nombre}"?`)) {
        await eliminarProductoApi(prod.id);
        await cargarProductos();
      }
    });

    tdAcciones.append(btnEditar, btnEliminar);
    tr.append(tdId, tdNombre, tdPrecio, tdCategoria, tdStock, tdEstado, tdAcciones);
    tbody.appendChild(tr);
  });
}

async function init(): Promise<void> {
  if (!session) return;

  await cargarCategorias();
  await cargarProductos();

  document.getElementById("btn-nuevo")?.addEventListener("click", () => {
    editandoId = null;
    showModal("Nuevo Producto");
  });

  document.getElementById("btn-cancelar")?.addEventListener("click", () => {
    hideModal();
  });

  document.getElementById("producto-form")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const errorEl = document.getElementById("form-error");

    const nombre = (document.getElementById("nombre") as HTMLInputElement).value.trim();
    const descripcion = (document.getElementById("descripcion") as HTMLTextAreaElement).value.trim();
    const precio = parseFloat((document.getElementById("precio") as HTMLInputElement).value);
    const stock = parseInt((document.getElementById("stock") as HTMLInputElement).value);
    const imagen = (document.getElementById("imagen") as HTMLInputElement).value.trim();
    const disponible = (document.getElementById("disponible") as HTMLInputElement).checked;
    const idCategoria = parseInt((document.getElementById("categoria") as HTMLSelectElement).value);

    try {
      if (editandoId !== null) {
        await actualizarProductoApi(editandoId, {
          nombre, descripcion, precio, stock, imagen, disponible, idCategoria,
        });
      } else {
        await crearProductoApi({
          nombre, descripcion, precio, stock, imagen, disponible, idCategoria,
        });
      }
      hideModal();
      await cargarProductos();
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
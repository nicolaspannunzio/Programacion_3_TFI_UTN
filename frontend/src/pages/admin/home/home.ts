import { getCategoriasApi, getProductosApi, getPedidosApi } from "../../../utils/api";
import { getSession, logout } from "../../../utils/auth";

const session = getSession();

if (!session || session.rol !== "ADMIN") {
  window.location.replace("/src/pages/auth/login/login.html");
}

async function init(): Promise<void> {
  if (!session) return;

  try {
    const [categorias, productos, pedidos] = await Promise.all([
      getCategoriasApi(),
      getProductosApi(),
      getPedidosApi(),
    ]);

    const totalCategoriasEl = document.getElementById("total-categorias");
    const totalProductosEl = document.getElementById("total-productos");
    const totalPedidosEl = document.getElementById("total-pedidos");
    const totalDisponiblesEl = document.getElementById("total-disponibles");

    if (totalCategoriasEl) totalCategoriasEl.textContent = String(categorias.length);
    if (totalProductosEl) totalProductosEl.textContent = String(productos.length);
    if (totalPedidosEl) totalPedidosEl.textContent = String(pedidos.length);
    if (totalDisponiblesEl) {
      const disponibles = productos.filter((p: any) => p.disponible && p.stock > 0).length;
      totalDisponiblesEl.textContent = String(disponibles);
    }

  } catch (error) {
    console.error("Error al cargar estadísticas:", error);
  }

  document.getElementById("logout-btn")?.addEventListener("click", () => logout());
}

init();
import { crearPedidoApi } from "../../../utils/api";
import { getSession } from "../../../utils/auth";
//import { navigate } from "../../../utils/navigate";
import type { CartItem } from "../../../types/product";

const CART_KEY = "foodstore_cart";

function getCart(): CartItem[] {
  const raw = localStorage.getItem(CART_KEY);
  return raw ? JSON.parse(raw) : [];
}

function saveCart(cart: CartItem[]): void {
  localStorage.setItem(CART_KEY, JSON.stringify(cart));
}

function clearCart(): void {
  localStorage.removeItem(CART_KEY);
}

function calcTotal(cart: CartItem[]): number {
  return cart.reduce(
    (acc, item) => acc + item.product.precio * item.cantidad,
    0
  );
}

function updateModalTotal(): void {
  const cart = getCart();
  const modalTotal = document.getElementById("modal-total");
  if (modalTotal) {
    modalTotal.textContent = `$${calcTotal(cart).toLocaleString("es-AR")}`;
  }
}

function render(): void {
  const cart = getCart();
  const listEl = document.getElementById("cart-list");
  const emptyMsg = document.getElementById("empty-msg");
  const footer = document.getElementById("cart-footer");
  const totalEl = document.getElementById("cart-total");

  if (!listEl || !emptyMsg || !footer || !totalEl) return;

  listEl.innerHTML = "";

  if (cart.length === 0) {
    emptyMsg.style.display = "block";
    footer.style.display = "none";
    return;
  }

  emptyMsg.style.display = "none";
  footer.style.display = "flex";

  cart.forEach((item) => {
    const subtotal = item.product.precio * item.cantidad;

    const div = document.createElement("div");
    div.className = "cart-item";

    const img = document.createElement("img");
    img.alt = item.product.nombre;
    img.src = item.product.imagen || "";
    img.onerror = () => {
      img.onerror = null;
      img.src = `https://placehold.co/80x80/ff6347/ffffff?text=🍔`;
    };

    const info = document.createElement("div");
    info.className = "info";

    const nombre = document.createElement("h3");
    nombre.textContent = item.product.nombre;

    const precioUnit = document.createElement("p");
    precioUnit.textContent = `$${item.product.precio.toLocaleString("es-AR")} c/u`;

    info.append(nombre, precioUnit);

    const controls = document.createElement("div");
    controls.className = "quantity-controls";

    const btnMinus = document.createElement("button");
    btnMinus.className = "btn-minus";
    btnMinus.textContent = "−";

    const cantSpan = document.createElement("span");
    cantSpan.textContent = String(item.cantidad);

    const btnPlus = document.createElement("button");
    btnPlus.className = "btn-plus";
    btnPlus.textContent = "+";

    controls.append(btnMinus, cantSpan, btnPlus);

    const subtotalDiv = document.createElement("div");
    subtotalDiv.className = "subtotal";
    subtotalDiv.textContent = `$${subtotal.toLocaleString("es-AR")}`;

    const btnRemove = document.createElement("button");
    btnRemove.className = "btn-remove";
    btnRemove.title = "Eliminar";
    btnRemove.textContent = "✕";

    div.append(img, info, controls, subtotalDiv, btnRemove);

    btnPlus.addEventListener("click", () => {
      item.cantidad += 1;
      saveCart(cart);
      render();
    });

    btnMinus.addEventListener("click", () => {
      if (item.cantidad > 1) {
        item.cantidad -= 1;
      } else {
        const index = cart.indexOf(item);
        cart.splice(index, 1);
      }
      saveCart(cart);
      render();
    });

    btnRemove.addEventListener("click", () => {
      const index = cart.indexOf(item);
      cart.splice(index, 1);
      saveCart(cart);
      render();
    });

    listEl.appendChild(div);
  });

  totalEl.textContent = `$${calcTotal(cart).toLocaleString("es-AR")}`;
}

function showModal(): void {
  const modal = document.getElementById("checkout-modal");
  if (modal) modal.style.display = "flex";
  updateModalTotal();
}

function hideModal(): void {
  const modal = document.getElementById("checkout-modal");
  if (modal) modal.style.display = "none";
}

async function confirmarPedido(): Promise<void> {
  const cart = getCart();
  const session = getSession();
  const errorEl = document.getElementById("checkout-error");
  const formaPago = (document.getElementById("forma-pago") as HTMLSelectElement).value;

  if (!session) {
    window.location.replace("../../auth/login/login.html");
    return;
  }

  if (cart.length === 0) return;

  const detallePedido = cart.map((item) => ({
    idProducto: item.product.id,
    cantidad: item.cantidad,
  }));

  try {
    await crearPedidoApi({
      idUsuario: session.id,
      estado: "PENDIENTE",
      formaPago,
      detallePedido,
    });

    clearCart();
    window.location.replace("/src/pages/client/orders/orders.html");
  } catch (error: any) {
    if (errorEl) {
      errorEl.textContent = error.message || "Error al confirmar el pedido.";
      errorEl.style.display = "block";
    }
  }
}

function init(): void {
  render();

  document.getElementById("btn-clear")?.addEventListener("click", () => {
    if (confirm("¿Vaciar el carrito?")) {
      clearCart();
      render();
    }
  });

  document.getElementById("btn-checkout")?.addEventListener("click", () => {
    showModal();
  });

  document.getElementById("btn-cancel-modal")?.addEventListener("click", () => {
    hideModal();
  });

  document.getElementById("checkout-form")?.addEventListener("submit", async (e) => {
    e.preventDefault();
    await confirmarPedido();
  });
}

init();
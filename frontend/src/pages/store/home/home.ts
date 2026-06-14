import { getCategoriasApi, getProductosApi } from "../../../utils/api";
import { logout } from "../../../utils/auth";
import type { ICategory } from "../../../types/category";
import type { Product, CartItem } from "../../../types/product";

const CART_KEY = "foodstore_cart";

let productos: Product[] = [];
let selectedCategory: number | "all" = "all";
let searchQuery = "";

// ==================== CARRITO ====================

function getCart(): CartItem[] {
  const raw = localStorage.getItem(CART_KEY);
  return raw ? JSON.parse(raw) : [];
}

function saveCart(cart: CartItem[]): void {
  localStorage.setItem(CART_KEY, JSON.stringify(cart));
}

function addToCart(product: Product): void {
  const cart = getCart();
  const existing = cart.find((item) => item.product.id === product.id);
  if (existing) {
    existing.cantidad += 1;
  } else {
    cart.push({ product, cantidad: 1 });
  }
  saveCart(cart);
  updateCartCount();
}

function updateCartCount(): void {
  const cart = getCart();
  const total = cart.reduce((acc, item) => acc + item.cantidad, 0);
  const badge = document.getElementById("cart-count");
  if (badge) badge.textContent = String(total);
}

// ==================== CATEGORIAS ====================

function renderCategories(categorias: ICategory[]): void {
  const list = document.getElementById("category-list");
  if (!list) return;

  // Limpiar categorías previas excepto "Todas"
  list.querySelectorAll("li:not([data-id='all'])").forEach((el) => el.remove());

  categorias.forEach((cat) => {
    const li = document.createElement("li");
    li.textContent = cat.nombre;
    li.dataset["id"] = String(cat.id);
    li.addEventListener("click", () => {
      selectedCategory = cat.id;
      setActiveCategory(li);
      renderProducts();
    });
    list.appendChild(li);
  });

  const allLi = list.querySelector<HTMLLIElement>('[data-id="all"]');
  if (allLi) {
    allLi.addEventListener("click", () => {
      selectedCategory = "all";
      setActiveCategory(allLi);
      renderProducts();
    });
  }
}

function setActiveCategory(activeLi: HTMLLIElement): void {
  document
    .querySelectorAll("#category-list li")
    .forEach((li) => li.classList.remove("active"));
  activeLi.classList.add("active");
}

// ==================== PRODUCTOS ====================

function getFilteredProducts(): Product[] {
  return productos.filter((p) => {
    const matchSearch = p.nombre
      .toLowerCase()
      .includes(searchQuery.toLowerCase());
    const matchCategory =
      selectedCategory === "all" || p.categoria?.id === selectedCategory;
    return matchSearch && matchCategory && !p.eliminado;
  });
}

function renderProducts(): void {
  const grid = document.getElementById("product-grid");
  const noResults = document.getElementById("no-results");
  if (!grid || !noResults) return;

  const filtered = getFilteredProducts();
  grid.querySelectorAll(".product-card").forEach((el) => el.remove());

  if (filtered.length === 0) {
    noResults.style.display = "block";
    return;
  }
  noResults.style.display = "none";

  filtered.forEach((product) => {
    const card = document.createElement("div");
    card.className = "product-card";

    const categoryName = product.categoria?.nombre ?? "";
    const isAvailable = product.disponible && product.stock > 0;

    const img = document.createElement("img");
    img.alt = product.nombre;
    img.src = product.imagen || "";
    img.onerror = () => {
      img.onerror = null;
      const textoSeguro = encodeURIComponent(categoryName || "Food Store");
      img.src = `https://placehold.co/300x140/ff6347/ffffff?text=${textoSeguro}`;
    };

    const tag = document.createElement("span");
    tag.className = "categoria-tag";
    tag.textContent = categoryName;

    const title = document.createElement("h3");
    title.textContent = product.nombre;

    const desc = document.createElement("p");
    desc.className = "desc";
    desc.textContent = product.descripcion;

    const precio = document.createElement("p");
    precio.className = "precio";
    precio.textContent = `$${product.precio.toLocaleString("es-AR")}`;

    const badge = document.createElement("span");
    badge.className = isAvailable ? "badge-disponible" : "badge-nodisponible";
    badge.textContent = isAvailable ? "Disponible" : "Sin stock";

    const btn = document.createElement("button");
    btn.className = "btn-primary";
    btn.disabled = !isAvailable;
    btn.textContent = isAvailable ? "Agregar al carrito" : "Sin stock";

    const msg = document.createElement("span");
    msg.className = "added-msg";

    if (isAvailable) {
      btn.addEventListener("click", () => {
        addToCart(product);
        msg.textContent = "✓ Agregado";
        setTimeout(() => (msg.textContent = ""), 1500);
      });
    }

    card.append(img, tag, title, desc, precio, badge, btn, msg);
    grid.appendChild(card);
  });
}

// ==================== INIT ====================

async function init(): Promise<void> {
  try {
    const [categorias, prods] = await Promise.all([
      getCategoriasApi(),
      getProductosApi(),
    ]);

    productos = prods;
    renderCategories(categorias);
    renderProducts();
    updateCartCount();
  } catch (error) {
    console.error("Error al cargar datos:", error);
  }

  const searchInput = document.getElementById("search-bar") as HTMLInputElement | null;
  searchInput?.addEventListener("input", () => {
    searchQuery = searchInput.value;
    renderProducts();
  });

  const logoutBtn = document.getElementById("logout-btn");
  logoutBtn?.addEventListener("click", () => logout());
}

init();
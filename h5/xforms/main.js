import "./main.css";
import "@jinntec/fore";

const fore = document.querySelector("fx-fore");

fore.addEventListener("refresh-done", () => {
    document.querySelectorAll("select[data-src]").forEach((sel) => {
        const items = document.querySelector(sel.dataset.src)?.querySelectorAll("li") ?? [];
        if (!items.length || sel.options.length === items.length) return;
        sel.replaceChildren(...[...items].map((li) => {
            const opt = document.createElement("option");
            opt.value = li.dataset.value;
            opt.textContent = li.textContent.trim();
            return opt;
        }));
        sel.value = sel.closest("fx-control")?.getModelItem()?.value ?? "";
    });
});


// Animacion: llevo el gallo del hero hasta el logo del header al hacer scroll

window.addEventListener("load", () => {

    const hero = document.getElementById("hero");
    const heroLogo = document.getElementById("heroLogo");
    const headerLogo = document.getElementById("headerLogo");

    if (!hero || !heroLogo || !headerLogo) return;

    heroLogo.style.willChange = "transform, opacity";
    headerLogo.style.transition = "opacity .35s ease";
    headerLogo.style.opacity = "0";
    headerLogo.style.visibility = "hidden";
    headerLogo.style.pointerEvents = "none";
    heroLogo.style.transformOrigin = "center center";

    const reduceMotion = matchMedia("(prefers-reduced-motion: reduce)").matches;
    const easeOut = (t) => 1 - Math.pow(1 - t, 2.4);

    // Espero que las imagenes esten listas para medir posiciones y tamaños
    const ensureLoaded = (el) =>
        el && el.tagName === "IMG"
            ? el.complete
                ? Promise.resolve()
                : new Promise((r) => el.addEventListener("load", r, { once: true }))
            : Promise.resolve();

    Promise.all([ensureLoaded(heroLogo), ensureLoaded(headerLogo)]).then(() => {
        let ticking = false;

        // Mide la distancia y escala entre heroLogo y headerLogo
        function measureTarget() {
            const prev = heroLogo.style.transform;
            heroLogo.style.transform = "translate3d(0,0,0) scale(1)";

            const a = heroLogo.getBoundingClientRect();
            const b = headerLogo.getBoundingClientRect();
            heroLogo.style.transform = prev;

            const ax = a.left + a.width / 2, ay = a.top + a.height / 2;
            const bx = b.left + b.width / 2, by = b.top + b.height / 2;
            const scale = a.width ? b.width / a.width : 1;
            return { dx: bx - ax, dy: by - ay, scale };
        }

        // Calcula y aplica la animación según el scroll
        function update() {
            ticking = false;
            const heroH = hero.offsetHeight || innerHeight;
            const maxScroll = Math.max(1, heroH * 0.9);
            const p = Math.min(Math.max(scrollY / maxScroll, 0), 1);

            const { dx, dy, scale } = measureTarget();
            const e = easeOut(p);

            heroLogo.style.transform = `translate3d(${dx * e}px, ${dy * e}px, 0) scale(${1 + (scale - 1) * e})`;
            heroLogo.style.opacity = String(1 - e);
            headerLogo.style.opacity = String(e);
            headerLogo.style.visibility = e > 0 ? "visible" : "hidden";
            headerLogo.style.pointerEvents = e > 0.9 ? "auto" : "none";
        }

        // Escucho scroll y actualizo solo una vez por frame (mejor rendimiento)
        function onScroll() {
            if (!ticking) {
                ticking = true;
                requestAnimationFrame(update);
            }
        }

        addEventListener("scroll", onScroll, { passive: true });
        addEventListener("resize", onScroll);
        update(); // primera ejecución
    });
});

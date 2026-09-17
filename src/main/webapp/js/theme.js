(function () {
    "use strict";

    const storageKey = "driveease-theme";
    const root = document.documentElement;

    function readSavedTheme() {
        try {
            return localStorage.getItem(storageKey);
        } catch (error) {
            return null;
        }
    }

    function saveTheme(theme) {
        try {
            localStorage.setItem(storageKey, theme);
        } catch (error) {
            // The switch still works if browser storage is unavailable.
        }
    }

    function applyTheme(theme, button) {
        root.dataset.theme = theme;

        if (button) {
            const isDark = theme === "dark";

            button.textContent = isDark
                ? "☀️ Light mode"
                : "🌙 Dark mode";

            button.setAttribute(
                "aria-label",
                isDark ? "Switch to light mode" : "Switch to dark mode"
            );

            button.setAttribute(
                "aria-pressed",
                String(isDark)
            );
        }
    }

    const savedTheme = readSavedTheme();
    const initialTheme = savedTheme === "dark" ? "dark" : "light";

    // Apply the saved theme before the page finishes rendering.
    applyTheme(initialTheme);

    document.addEventListener("DOMContentLoaded", function () {
        const button = document.createElement("button");

        button.type = "button";
        button.className = "driveease-theme-toggle";

        applyTheme(root.dataset.theme, button);

        button.addEventListener("click", function () {
            const nextTheme = root.dataset.theme === "dark"
                ? "light"
                : "dark";

            applyTheme(nextTheme, button);
            saveTheme(nextTheme);
        });

        document.body.appendChild(button);
    });
})();
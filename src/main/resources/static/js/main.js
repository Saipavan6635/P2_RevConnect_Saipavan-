// RevConnect - Main JavaScript
// Handles alerts, lightweight helpers, and confirmation prompts.

document.addEventListener('DOMContentLoaded', function () {
    function applyTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('revconnect_theme', theme);
        document.querySelectorAll('[data-theme-toggle]').forEach(function (btn) {
            btn.textContent = theme === 'dark' ? 'Light' : 'Dark';
        });
    }

    var savedTheme = localStorage.getItem('revconnect_theme');
    if (savedTheme === 'dark' || savedTheme === 'light') {
        applyTheme(savedTheme);
    } else {
        var prefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
        applyTheme(prefersDark ? 'dark' : 'light');
    }

    document.querySelectorAll('[data-theme-toggle]').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var current = document.documentElement.getAttribute('data-theme') || 'light';
            applyTheme(current === 'dark' ? 'light' : 'dark');
        });
    });

    document.querySelectorAll('.alert').forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () { alert.remove(); }, 500);
        }, 4000);
    });

    const postTextarea = document.getElementById('postContent');
    const charCounter = document.getElementById('charCounter');
    if (postTextarea && charCounter) {
        postTextarea.addEventListener('input', function () {
            const count = this.value.length;
            charCounter.textContent = count + ' / 500';
            charCounter.style.color = count > 450 ? '#b91c1c' : '#71717a';
        });
    }

    const contentField = document.getElementById('postContent');
    const hashtagField = document.getElementById('hashtagField');
    if (contentField && hashtagField) {
        contentField.addEventListener('blur', function () {
            const matches = this.value.match(/#\w+/g);
            if (matches) {
                const tags = [...new Set(matches)].join(', ');
                if (!hashtagField.value) {
                    hashtagField.value = tags;
                }
            }
        });
    }

    const advancedToggle = document.getElementById('advancedToggle');
    const advancedOptions = document.getElementById('advancedOptions');
    if (advancedToggle && advancedOptions) {
        advancedToggle.addEventListener('click', function () {
            const isHidden = advancedOptions.style.display === 'none' || !advancedOptions.style.display;
            advancedOptions.style.display = isHidden ? 'block' : 'none';
            this.textContent = isHidden ? 'Fewer options' : 'More options';
        });
    }

    document.querySelectorAll('.confirm-delete').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            if (!confirm('Are you sure you want to delete this?')) {
                e.preventDefault();
            }
        });
    });

    const topBtn = document.getElementById('scrollTopBtn');
    if (topBtn) {
        window.addEventListener('scroll', function () {
            topBtn.style.display = window.scrollY > 400 ? 'flex' : 'none';
        });
        topBtn.addEventListener('click', function () {
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    }
});

import forms from '@tailwindcss/forms'

/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts}'],
  theme: {
    extend: {
      fontFamily: {
        display: ['"Playfair Display"', 'Georgia', 'serif'],
        body: ['"Crimson Pro"', 'Georgia', 'serif'],
        mono: ['"JetBrains Mono"', 'monospace'],
      },
      colors: {
        ink: {
          DEFAULT: '#1a1208',
          light: '#3d2e1a',
        },
        amber: {
          DEFAULT: '#c8860a',
          light: '#e6a820',
          pale: '#f5e8c8',
        },
        cream: {
          DEFAULT: '#faf6ee',
          dark: '#f0e8d4',
        },
        paper: {
          DEFAULT: '#ede0c4',
          dark: '#d4c5a0',
        },
        danger: '#8b2318',
        teal: '#1a5c5c',
      },
      boxShadow: {
        card: '0 2px 8px rgba(26,18,8,0.10)',
        modal: '0 32px 80px rgba(26,18,8,0.35)',
        btn: '0 6px 20px rgba(26,18,8,0.30)',
      },
    },
  },
  plugins: [forms],
}

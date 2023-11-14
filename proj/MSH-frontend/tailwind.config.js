/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {},
  },
  plugins: [require('daisyui')],
  daisyui: {
    themes: [
      {
        light: {
          primary: '#4abfbd',
          secondary: '#cdedef',
          accent: '#38848a',
          neutral: '#0f3d3d',
          'base-100': '#fbfefe',
          success: '#8ab17d',
          warning: '#f4a261',
          error: '#e76f51',
        },
      },
    ],
  },
};

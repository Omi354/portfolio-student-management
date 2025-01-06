import { createTheme } from '@mui/material/styles'

// Create a theme instance.
const theme = createTheme({
  palette: {
    primary: {
      main: '#699186',
    },
    secondary: {
      main: '#ecb356',
    },
    error: {
      main: '#ea614d',
    },
    info: {
      main: '#efe3d4',
    },
  },
})

export default theme

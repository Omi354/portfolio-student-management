import { Box } from '@mui/material'
import Image from 'next/image'

const Loading: React.FC = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '100vh',
      }}
    >
      <Image src="/loading.svg" width={150} height={150} alt="loading..." />
    </Box>
  )
}

export default Loading

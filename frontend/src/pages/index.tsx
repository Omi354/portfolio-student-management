import { Box, Button } from '@mui/material'
import { NextPage } from 'next'
import Image from 'next/image'
import { useRouter } from 'next/router'
import localImage from '../images/student-management.gif'

const TopPage: NextPage = () => {
  const router = useRouter()
  return (
    <Box sx={{ backgroundColor: '#fcfcfc', minHeight: '100vh', py: 4 }}>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          textAlign: 'center',
        }}
      >
        <Image src={localImage} alt="Top Image" width={1000} height={500} />
        <Button
          variant="contained"
          color="primary"
          size="large"
          sx={{ fontWeight: 'bold', color: 'white' }}
          onClick={() => router.push('/students')}
        >
          ゲストログイン
        </Button>
      </Box>
    </Box>
  )
}

export default TopPage

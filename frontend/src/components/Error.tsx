import { Card, CardContent, Container } from '@mui/material'

type ErrorProps = {
  error: {
    response?: {
      data?: {
        error?: string
        message?: string
      }
    }
  }
}

const Error: React.FC<ErrorProps> = ({ error }) => {
  return (
    <Container maxWidth="sm">
      <Card sx={{ p: 3, mt: 8, backgroundColor: '#EEEEEE' }}>
        <CardContent sx={{ lineHeight: 2 }}>
          {error?.response?.data?.message
            ? error.response.data.message
            : '現在、システムに技術的な問題が発生しています。ご不便をおかけして申し訳ありませんが、復旧までしばらくお待ちください。'}
        </CardContent>
      </Card>
    </Container>
  )
}

export default Error

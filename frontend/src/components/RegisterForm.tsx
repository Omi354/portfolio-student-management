import {
  Box,
  Button,
  FormControl,
  FormHelperText,
  Grid2,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from '@mui/material'

import { Control, Controller } from 'react-hook-form'
import { StudentDetailProps } from '@/pages'

type RegisterFormProps = {
  control: Control<StudentDetailProps>
  onSubmit: () => void
  onClick: () => void
}

const RegisterForm: React.FC<RegisterFormProps> = ({
  control,
  onSubmit,
  onClick,
}) => {
  const validationRules = {
    student: {
      fullName: {
        required: '氏名は必須です',
      },
      kana: {
        pattern: {
          // eslint-disable-next-line no-irregular-whitespace
          value: /^$|^[ァ-ヶー\s　]+$/,
          message: 'カナ名はカタカナとスペースのみを入力してください',
        },
      },
      email: {
        required: 'メールアドレスは必須です',
        pattern: {
          value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
          message: '正しい形式のメールアドレスを入力してください。',
        },
      },
      city: {
        required: '居住地域は必須です',
      },
      age: {
        min: {
          value: 0,
          message: '年齢は0歳以上である必要があります',
        },
        max: {
          value: 150,
          message: '年齢は150歳以下である必要があります',
        },
      },
      gender: {
        validate: (value: string) => {
          return (
            ['Male', 'Female', 'NON_BINARY', ''].includes(value) ||
            '性別はMale, Female, NON_BINARY のいずれかを指定してください'
          )
        },
      },
    },
    studentCourseList: {
      courseName: {
        required: 'コース名は必須です',
      },
      enrollmentStatus: {
        status: {
          required: '申込状況は必須です',
          validate: (value: string) => {
            return (
              ['仮申込', '本申込', '受講中', '受講終了'].includes(value) ||
              '申込状況は仮申込, 本申込, 受講中, 受講終了 のいずれかを指定してください'
            )
          },
        },
      },
    },
  }
  return (
    <Box sx={{ m: 2 }}>
      <Grid2 container component="form" spacing={2}>
        <Grid2 size={6}>
          <Controller
            name="student.fullName"
            control={control}
            rules={validationRules.student.fullName}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="text"
                label="氏名"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Controller
            name="student.kana"
            control={control}
            rules={validationRules.student.kana}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="text"
                label="カナ名"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Controller
            name="student.nickName"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                type="text"
                label="ニックネーム"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Controller
            name="student.email"
            control={control}
            rules={validationRules.student.email}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="email"
                label="メールアドレス"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Controller
            name="student.city"
            control={control}
            rules={validationRules.student.city}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="text"
                label="居住地域"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Controller
            name="student.age"
            control={control}
            rules={validationRules.student.age}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="number"
                label="年齢"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={4}>
          <Controller
            name="student.gender"
            control={control}
            rules={validationRules.student.gender}
            render={({ field, fieldState }) => (
              <FormControl sx={{ width: '100%' }} error={fieldState.invalid}>
                <InputLabel id="gender">性別</InputLabel>

                <Select {...field} labelId="gender" label="gender">
                  <MenuItem value={'Male'}>Male</MenuItem>

                  <MenuItem value={'Female'}>Female</MenuItem>

                  <MenuItem value={'NON_BINARY'}>NON_BINARY</MenuItem>
                </Select>
                <FormHelperText>{fieldState.error?.message}</FormHelperText>
              </FormControl>
            )}
          />
        </Grid2>

        <Grid2 size={4}>
          <Controller
            name={`studentCourseList.${0}.courseName`}
            control={control}
            rules={validationRules.studentCourseList.courseName}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="text"
                label="コース名"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={4}>
          <Controller
            name={`studentCourseList.${0}.enrollmentStatus.status`}
            control={control}
            rules={validationRules.studentCourseList.enrollmentStatus.status}
            render={({ field, fieldState }) => (
              <FormControl sx={{ width: '100%' }} error={fieldState.invalid}>
                <InputLabel id="status">申込状況</InputLabel>

                <Select {...field} labelId="status" label="status">
                  <MenuItem value={'仮申込'}>仮申込</MenuItem>
                  <MenuItem value={'本申込'}>本申込</MenuItem>
                  <MenuItem value={'受講中'}>受講中</MenuItem>
                  <MenuItem value={'受講終了'}>受講終了</MenuItem>
                </Select>
                <FormHelperText>{fieldState.error?.message}</FormHelperText>
              </FormControl>
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Button
            variant="contained"
            type="button"
            size="large"
            onClick={onSubmit}
            sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
          >
            登録
          </Button>
        </Grid2>

        <Grid2 size={6}>
          <Button
            variant="contained"
            type="button"
            color="error"
            size="large"
            onClick={onClick}
            sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
          >
            キャンセル
          </Button>
        </Grid2>
      </Grid2>
    </Box>
  )
}

export default RegisterForm

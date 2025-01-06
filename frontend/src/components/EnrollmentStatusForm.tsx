import {
  Box,
  Grid2,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
} from '@mui/material'
import { Controller, UseFormReturn } from 'react-hook-form'
import { EnrollmentStatusFormData } from '@/types'

type EnrollmentStatusFormProps = {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  formHandler: UseFormReturn<EnrollmentStatusFormData, any, undefined>
  onSubmit: () => void
  onCancel: () => void
}

const EnrollmentStatusForm: React.FC<EnrollmentStatusFormProps> = ({
  formHandler,
  onSubmit,
  onCancel,
}) => {
  return (
    <Box sx={{ m: 2 }}>
      <Grid2 container component="form" spacing={2}>
        <Grid2 size={12}>
          <Controller
            name="studentCourseId"
            control={formHandler.control}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                disabled={true}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="text"
                label="コースID"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>
        <Grid2 size={12}>
          <FormControl sx={{ width: '100%' }}>
            <InputLabel id="status">申込状況</InputLabel>
            <Controller
              name="status"
              control={formHandler.control}
              render={({ field }) => (
                <Select {...field} labelId="status" label="status">
                  <MenuItem value={'仮申込'}>仮申込</MenuItem>
                  <MenuItem value={'本申込'}>本申込</MenuItem>
                  <MenuItem value={'受講中'}>受講中</MenuItem>
                  <MenuItem value={'受講終了'}>受講終了</MenuItem>
                </Select>
              )}
            />
          </FormControl>
        </Grid2>
        <Grid2 size={6}>
          <Button
            variant="contained"
            type="button"
            color="error"
            size="large"
            onClick={onCancel}
            sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
          >
            キャンセル
          </Button>
        </Grid2>
        <Grid2 size={6}>
          <Button
            variant="contained"
            type="button"
            size="large"
            onClick={onSubmit}
            sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
          >
            更新
          </Button>
        </Grid2>
      </Grid2>
    </Box>
  )
}

export default EnrollmentStatusForm

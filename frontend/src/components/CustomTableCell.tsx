import { TableCell } from '@mui/material'
import { ReactNode } from 'react'

type CustomTableCellProps = {
  minWidth: number
  maxWidth: number
  children: ReactNode
}

const CustomTableCell = ({
  minWidth,
  maxWidth,
  children,
}: CustomTableCellProps) => {
  return (
    <TableCell
      sx={{
        minWidth: `${minWidth}px`,
        maxWidth: `${maxWidth}px`,
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        whiteSpace: 'nowrap',
      }}
    >
      {children}
    </TableCell>
  )
}

export default CustomTableCell

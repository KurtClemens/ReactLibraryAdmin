import { Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Button } from '@mui/material'
import styles from './ConfirmDialog.module.css'

type ConfirmationProps={
    confirm: any
    setConfirm: any
}

const ConfirmDialog = ({confirm, setConfirm}:ConfirmationProps) => {
  return (
      <Dialog className={styles['dialog-content']} open={confirm.isOpen}>
          <DialogTitle id='dialog-title'>{confirm.title}</DialogTitle>
          <DialogContent >
              <DialogContentText id='dialog-description'>{confirm.subTitle}</DialogContentText>
          </DialogContent>
          <DialogActions>
              <Button className={styles['card-button']} color="primary" variant="outlined" onClick={confirm.onConfirm}>Yes</Button>
              <Button className={styles['card-button']} color="primary" variant="outlined" autoFocus onClick={()=> setConfirm({...confirm, isOpen:false})}>No</Button>
          </DialogActions>
      </Dialog>
  )
}

export default ConfirmDialog
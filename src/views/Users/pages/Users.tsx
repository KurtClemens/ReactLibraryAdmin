import { Button, createTheme } from '@mui/material';
import styles from './Users.module.css'
import { Add } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import UserTable from '../components/userTable/UserTable';

const Users = () => {
  const navigate = useNavigate();

  function add() {
    navigate('/users/add')
  }

  return (
    <>
      <div className={styles['screen-title']}>Users</div>
      <div className={styles['button-top-right-above-card']}>
        <Button className={styles['card-button']} color="primary" variant="outlined"
          onClick={add} startIcon={<Add></Add>}>add</Button>
      </div >
      <UserTable></UserTable>
    </>
  )
}

export default Users
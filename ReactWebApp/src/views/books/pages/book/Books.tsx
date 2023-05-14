import styles from './Books.module.css'
import BookTable from '../../components/bookTable/BookTable';
import { Button } from '@mui/material';
import { Add } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

const Books = () => {
  const navigate = useNavigate();

  function add() {
    navigate('/books/add')
  }

  return (
    <>
      <div className={styles['screen-title']}>Books</div>
      <div className={styles['button-top-right-above-card']}>
        <Button className={styles['card-button']} color="primary" variant="outlined"
          onClick={add} startIcon={<Add></Add>}>add</Button>
      </div >
      <BookTable></BookTable>
    </>
  )
}

export default Books
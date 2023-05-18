import { Grid } from '@mui/material';
import BooksCard from '../components/cards/BooksCard';
import OverdueCard from '../components/cards/OverdueCard';
import RequestCard from '../components/cards/RequestCard';
import UserCard from '../components/cards/UserCard';
import styles from './Dashboard.module.css'
import Layout from '../../layout/Layout';


const Dashboard = () => {
  return (
    <>
    <Layout>
      <div className={styles['screen-title']}>Dashboard</div>
      <Grid className={styles['grid-container']} container>
          <Grid className={styles['grid-column']}>
              <BooksCard></BooksCard>
              <OverdueCard></OverdueCard>
          </Grid >
          <Grid className={styles['grid-column']}>
              <RequestCard></RequestCard>
              <UserCard></UserCard>
          </Grid>
      </Grid>
      </Layout>
      </>
  )
}

export default Dashboard
import styles from './Requests.module.css'
import RequestTable from '../components/requestTable/RequestTable';
import Layout from '../../layout/Layout';


const Requests = () => {

  return (
    <>
      <Layout>
      <div className={styles['screen-title']}>Requests</div>
      <RequestTable ></RequestTable>
      </Layout>
    </>
  )
}

export default Requests
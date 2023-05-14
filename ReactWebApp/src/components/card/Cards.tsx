import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import styles from './Cards.module.css'
import { ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';

type CardPorps = {
    title:string,
    children: ReactNode
}

const Cards = ({ title, children }: CardPorps) => {
    const navigate = useNavigate();

    function navigateTo(){
        navigate(`/${title.toLowerCase()}`)
    }

    return (
        <>
        <div className={styles['card-container']}>
            <Card className={styles['card']}>
                <CardHeader title={title}>
                </CardHeader>
                <CardContent>
                    <Typography variant="h5" component="div">
                        {children}
                    </Typography>
                </CardContent>
            </Card>
            <CardActions className={styles['card-actions']}>
                <Button className={styles['card-button']} variant='contained' color="primary" onClick={navigateTo}>More details</Button>
            </CardActions>
            </div>
        </>
    );
}

export default Cards
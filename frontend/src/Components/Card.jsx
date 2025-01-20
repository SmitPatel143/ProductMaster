import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

export default function MediaCard({ image, title, description, button1Text, button2Text, onClickAddToCart, onClickLearnMore }) {
    return (
        <Card sx={{ maxWidth: 345, marginTop: 5, borderRadius: 3, boxShadow: 3, transition: 'transform 0.3s', '&:hover': { transform: 'scale(1.05)', boxShadow: 6 } }}>
            <CardMedia
                sx={{
                    height: 140,
                    borderTopLeftRadius: 3,
                    borderTopRightRadius: 3,
                    objectFit: 'cover',
                    transition: 'all 0.3s ease-in-out',
                    '&:hover': { transform: 'scale(1.1)' }
                }}
                image={image}
                title={title}
            />
            <CardContent sx={{ padding: 2 }}>
                <Typography gutterBottom variant="h5" component="div" sx={{ fontWeight: 'bold', color: '#2C3E50' }}>
                    {title}
                </Typography>
                <Typography variant="body2" sx={{ color: 'text.secondary', fontSize: '0.875rem', lineHeight: 1.5 }}>
                    {description}
                </Typography>
            </CardContent>
            <CardActions sx={{ padding: '8px 16px', justifyContent: 'space-between' }}>
                <Button
                    size="small"
                    sx={{ backgroundColor: '#3498DB', color: '#fff', '&:hover': { backgroundColor: '#2980B9' } }}
                    onClick={onClickAddToCart}  // Trigger the handler when clicked
                >
                    {button1Text}
                </Button>
                <Button
                    size="small"
                    sx={{ backgroundColor: '#2ECC71', color: '#fff', '&:hover': { backgroundColor: '#27AE60' } }}
                    onClick={onClickLearnMore}  // Trigger the handler when clicked
                >
                    {button2Text}
                </Button>
            </CardActions>
        </Card>
    );
}

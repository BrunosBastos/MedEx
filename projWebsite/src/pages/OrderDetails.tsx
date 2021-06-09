import {useEffect,useState} from 'react';
import { Helmet } from 'react-helmet';
import getInitials from 'src/utils/getInitials';
import products from 'src/__mocks__/products'
import order from 'src/__mocks__/order';
import { makeStyles } from '@material-ui/core/styles';
import StarRatings from 'react-star-ratings';
import {Avatar,
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    Divider,
    Typography,
    Container,
    Grid,
    TextField,
    CardHeader,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
} from '@material-ui/core';

const styles = makeStyles ({
    root: {
      width: "100%",
      overflowX: "auto",
      
    },
    table: {
        minWidth: 820,
        marginLeft: "5%",
    }
  });
const OrderDetails = () => {
    const [price, setPrice] = useState("0.00");
    // maybe useful to check later on, if a user has already reviewed a product
    const [review, setReview] = useState(null);
    const  classes  = styles();
    const [rating, setRating] = useState(0);
    const [reviewdescription, setReviewDescription] = useState("");
    const getTotalPrice = () => {
        let totalPrice = 0;
        for (let i = 0; i < products.length; i++) {
            totalPrice += products[i].price * products[i].quantity;
        }
        setPrice(totalPrice.toFixed(2))
    }
    const changeRating = ( newRating, name ) => {
        setRating(newRating);
    }
   
    const handleReviewText = (event) => {
        setReviewDescription(event.target.value)
    } 

    useEffect(() => {
        getTotalPrice()
    }, [products])
    return(
        <>
            <Helmet>
            <title>Order Details</title>
        </Helmet>
        <Box
            sx={{
            backgroundColor: 'background.default',
            minHeight: '100%',
            py: 6
            }}
        >
            <Grid container spacing={8}>
        <Grid
                item
                lg={8}
                md={8}
                xs={8}
            className={classes.root}
            >
                <form
                autoComplete="off"
                noValidate
                >
                <Card className={classes.table}>
                    <CardHeader
                    title="Ordered Products"
                    />
                    <Divider />
                    <CardContent>
                    <Grid
                        container
                        spacing={3}
                    >
                        <Grid
                        item
                        md={12}
                        xs={12}
                        >
                    <Table>
                        <TableHead>
                        <TableRow>
                            <TableCell>
                            Name
                            </TableCell>
                            <TableCell>
                            Reference
                            </TableCell>
                            <TableCell>
                            Supplier
                            </TableCell>
                            <TableCell>
                            Price
                            </TableCell>
                            <TableCell>
                            Quantity
                            </TableCell>
                            <TableCell>
                            Total
                            </TableCell>
                        </TableRow>
                        </TableHead>
                        <TableBody>
                            {products.map((product) => (
                                <>
                                <TableRow
                                hover
                                key={product.id}
                                >
                                <TableCell>
                                    <Box
                                    sx={{
                                        alignItems: 'center',
                                        display: 'flex'
                                    }}
                                    >
                                    <Avatar
                                        src={product.image}
                                        sx={{ mr: 2 }}
                                    >
                                        {getInitials(product.name)}
                                    </Avatar>
                                    <Typography
                                        color="textPrimary"
                                        variant="body1"
                                    >
                                        {product.name}
                                    </Typography>
                                    </Box>
                                </TableCell>
                                <TableCell>
                                    {product.id}
                                </TableCell>
                                <TableCell>
                                    {product.supplier.name}
                                </TableCell>
                                <TableCell>
                                    {product.price}
                                </TableCell>
                                <TableCell>
                                    
                                    {product.quantity}
                                </TableCell>
                                <TableCell>
                                    {(product.quantity * product.price).toFixed(2)}
                                </TableCell>
                                </TableRow>
                                </>
                                
                            ))}
                            <TableRow>
                                    <TableCell colSpan={5}>
                                    <Typography
                                            alignContent='left'
                                            color="textPrimary"
                                            variant="h6"
                                            >
                                            Total
                                    </Typography>
                                    </TableCell>
                                    <TableCell align="left"  style={{'marginRight': '10%'}}>
                                    <Typography
                                            alignContent='left'
                                            color="textPrimary"
                                            variant="h5"
                                            >
                                                    {price}€
                                                    </Typography>
                                    </TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </Grid>
                    </Grid>
                    </CardContent>
                    <Divider />
                    
                </Card>
                </form>
            </Grid>
            <Grid
                item
                lg={4}
                md={4}
                xs={4}
            >
            <Card style= {{'marginRight': '10%'}}>
            <CardHeader
                    title="Order Details"
                    />
                    <Divider />
                <CardContent>
                    <Grid
                        item
                        sm={12}
                        md={12}
                        xs={12}
                        >
                        <div>
                            <Typography variant="overline" display="block" gutterBottom>
                                order reference
                            </Typography>
                            <Typography variant="body2" display="block" gutterBottom>
                                {order.id}
                            </Typography>
                        </div>
                    </Grid>
                    
                        <div>
                            <Typography variant="overline" display="block" gutterBottom>
                                order date
                            </Typography>
                            <Typography variant="body2" display="block" gutterBottom>
                                {order.order_date}
                            </Typography>
                        </div>
                        <div>
                            <Typography variant="overline" display="block" gutterBottom>
                                Order Location
                            </Typography>
                            <Typography variant="body2" display="block" gutterBottom>
                                {order.lat},{order.lon}
                            </Typography>
                        </div>
                    {order.delivered && !review ?
                            <div>
                                <Typography variant="h5" display="block" gutterBottom>
                                    Make a  Review of your Order!
                                </Typography>
                                <TextField
                                    fullWidth
                                    id="standard-multiline-static"
                                    multiline
                                    placeholder="write a description of your experience."
                                    rows={4}
                                    defaultValue={reviewdescription}
                                    onChange={handleReviewText}
                                />
                                <Box style={{'paddingLeft':'10%'}}>
                                <StarRatings 
                                    rating={rating}
                                    starRatedColor="orange"
                                    starHoverColor="orange"
                                    changeRating={changeRating}
                                    numberOfStars={5}
                                    name='rating'
                                />
                                </Box>
                            {reviewdescription!= ""? 
                            <>
                            <Box display="flex" flexDirection="column" >
                            <Button  
                            color="primary"
                            variant="contained">
                            Submit
                            </Button>
                            </Box>
                            
                            </>
                            :
                            ""
                            }
                            </div>
                    : ""}
                </CardContent>
            </Card>
            </Grid>
            </Grid>
            </Box>
        </>
    )
}
export default OrderDetails;
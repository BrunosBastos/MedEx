import {useEffect,useState} from 'react';
import { Helmet } from 'react-helmet';
import getInitials from 'src/utils/getInitials';
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
import { toast } from 'react-toastify';

import purchaseService from 'src/services/purchaseService';


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

  const notifySuccess = (msg) => {
    toast.success(msg, {
      position: toast.POSITION.TOP_CENTER
      });
  }
  
  const notifyError = (msg) => {
    toast.error(msg, {
      position: toast.POSITION.TOP_CENTER
      });
  }
const OrderDetails = () => {
    const [price, setPrice] = useState("0.00");
    // maybe useful to check later on, if a user has already reviewed a product
    const [review, setReview] = useState(null);
    const  classes  = styles();
    const [order, setOrder] = useState(null);
    const [rating, setRating] = useState(0);
    const purchase_id = window.location.pathname.split('/').pop();
    const [reviewDescription, setReviewDescription] = useState("");
    const getTotalPrice = (order) => {
        let totalPrice = 0;
        for (let i = 0; i < order?.products?.length; i++) {
            let product = order.products[i].product;
            totalPrice += product.price * order.products[i].productAmount;
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
        purchaseService.getPurchaseDetails(purchase_id)
        .then( (res) => {
            return res.json();
        })
        .then( ( res) => {
            if(!res.errors){
                setOrder(res);
                getTotalPrice(res)
                console.log(res)
            }
        })        
    }, [])

    const submitReview = () => {
        console.log(purchase_id)
        console.log(reviewDescription)
        console.log(rating)
        purchaseService.makeReview("", purchase_id, reviewDescription, rating)
            .then( (res) => {
                console.log(res)
                if (res.status==201)
                    return res.json();
                return null
            })
            .then( ( res) => {
                console.log(res)
                if(res){
                    console.log(res)
                    notifySuccess("Successfully made a review")
                } else {
                    notifyError("Something went wrong!")
                }
            })      
    }
    
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
            {order? 
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
                            {order?.products?.map((product) => (
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
                                        src={product.product.image}
                                        sx={{ mr: 2 }}
                                    >
                                        {getInitials(product.product.name)}
                                    </Avatar>
                                    <Typography
                                        color="textPrimary"
                                        variant="body1"
                                    >
                                        {product.product.name}
                                    </Typography>
                                    </Box>
                                </TableCell>
                                <TableCell>
                                    {product.product.id}
                                </TableCell>
                                <TableCell>
                                    {product.product.supplier.name}
                                    <p>Latitude: {product.product.supplier.lat}</p>
                                    <p>Longitude: {product.product.supplier.lon}</p>
                                </TableCell>
                                <TableCell>
                                    {product.product.price}
                                </TableCell>
                                <TableCell>
                                    
                                    {product.productAmount}
                                </TableCell>
                                <TableCell>
                                    {(product.productAmount * product.product.price).toFixed(2)}
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
                : ""}
            </Grid>
            {order ? 
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
                                {order.orderDate}
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
                                    defaultValue={reviewDescription}
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
                            {reviewDescription!= ""? 
                            <>
                            <Box display="flex" flexDirection="column" >
                            <Button  
                            color="primary"
                            variant="contained"
                            onClick={() => submitReview()}
                            >
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
            : ""}
            </Grid>
            </Box>
        </>
    )
}
export default OrderDetails;
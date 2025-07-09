import React from "react";

const ProductPrice = ({ currency, price }) => {

    return (
        <>
        {currency}
        <span>{price}</span>
        
        </>
    )
}

export default ProductPrice;